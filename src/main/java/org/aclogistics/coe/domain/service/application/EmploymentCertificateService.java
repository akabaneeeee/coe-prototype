package org.aclogistics.coe.domain.service.application;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aclogistics.coe.domain.dto.application.ApplyCertificateDto;
import org.aclogistics.coe.domain.dto.application.CertificateApplicationDetails;
import org.aclogistics.coe.domain.dto.application.CertificateApplicationMilestoneDetails;
import org.aclogistics.coe.domain.dto.application.GeneratedCertificateDetails;
import org.aclogistics.coe.domain.dto.application.UpdateApplicationDetails;
import org.aclogistics.coe.domain.dto.application.additionalinfo.AdditionalInfo;
import org.aclogistics.coe.domain.dto.application.additionalinfo.HasLineManagerEmail;
import org.aclogistics.coe.domain.dto.application.filtered.GetFilteredApplicationDto;
import org.aclogistics.coe.domain.dto.application.filtered.PaginatedApplicationDto;
import org.aclogistics.coe.domain.dto.transition.StateTransition;
import org.aclogistics.coe.domain.dto.transition.StateVariable;
import org.aclogistics.coe.domain.dto.transition.TransitionApplicationDto;
import org.aclogistics.coe.domain.model.GeneratedCertificate;
import org.aclogistics.coe.domain.model.enumeration.Status;
import org.aclogistics.coe.domain.exception.RecordNotFoundException;
import org.aclogistics.coe.domain.model.CertificateApplication;
import org.aclogistics.coe.domain.model.CertificateApplicationMilestone;
import org.aclogistics.coe.domain.port.ICertificateApplicationRepository;
import org.aclogistics.coe.domain.port.IFileRepository;
import org.aclogistics.coe.domain.port.ILatestApplicationDetailsRepository;
import org.aclogistics.coe.domain.port.IWorkflowService;
import org.aclogistics.coe.domain.service.idgenerator.IdGenerator;
import org.aclogistics.coe.domain.service.publisher.IDomainEventPublisher;
import org.aclogistics.coe.domain.utility.DateTimeHelper;
import org.aclogistics.coe.domain.utility.NumberUtility;
import org.aclogistics.coe.infrastructure.jpa.application.exception.DuplicateReferenceNumberException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

/**
 * @author Rosendo Coquilla
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EmploymentCertificateService implements IEmploymentCertificateService {

    private static final String REFERENCE_NUMBER_PREFIX = "COE-";
    private static final TypeReference<Map<String, String>> TYPE_REFERENCE = new TypeReference<>() {};

    private final ICertificateApplicationRepository certificateApplicationRepository;
    private final ILatestApplicationDetailsRepository  latestApplicationDetailsRepository;
    private final IFileRepository fileRepository;
    private final ObjectMapper mapper;
    private final IWorkflowService workflowService;
    private final IDomainEventPublisher  domainEventPublisher;

    @Override
    @Retryable(retryFor = DuplicateReferenceNumberException.class, backoff = @Backoff(delay = 1000))
    public void apply(ApplyCertificateDto dto) {
        if (Objects.isNull(dto)) {
            throw new IllegalArgumentException("Please provide a valid certificate application form");
        }

        LocalDateTime now = DateTimeHelper.getTodayInPHTDefaultFormat();
        CertificateApplication newApplication = CertificateApplication.builder()
            .requesterEmail(dto.getRequesterEmail())
            .referenceNumber(IdGenerator.generate(REFERENCE_NUMBER_PREFIX))
            .firstName(dto.getFirstName().trim())
            .lastName(dto.getLastName().trim())
            .middleInitial(dto.getMiddleInitial().trim())
            .businessUnit(dto.getBusinessUnit())
            .hiredDate(dto.getHiredDate())
            .position(dto.getPosition())
            .department(dto.getDepartment())
            .employmentStatus(dto.getEmploymentStatus())
            .withCompensation(dto.getWithCompensation())
            .purpose(dto.getPurpose())
            .addressee(dto.getAddressee().trim())
            .placeOfAddressee(StringUtils.defaultIfBlank(dto.getPlaceOfAddressee(), null))
            .requestedBy(dto.getRequestedBy())
            .requestedDt(now)
            .build();

        AdditionalInfo additionalInfo = dto.getAdditionalInfo();
        if (additionalInfo instanceof HasLineManagerEmail details) {
            newApplication.setLineManagerEmail(details.getLineManagerEmail());
            details.setLineManagerEmail(null);
        }

        newApplication.setAdditionalInfo(mapper.convertValue(additionalInfo, TYPE_REFERENCE));
        newApplication.initializeMilestone(dto.getRequestedBy(), now);

        certificateApplicationRepository.save(newApplication);
    }

    @Override
    public void transitionToNextStatus(TransitionApplicationDto dto) {
        if (Objects.isNull(dto)) {
            throw new IllegalArgumentException("Please provide a valid transition request");
        }

        var givenReferenceNumber = dto.getReferenceNumber();
        CertificateApplication foundApplication = certificateApplicationRepository.findByReferenceNumber(givenReferenceNumber);

        if (Objects.isNull(foundApplication)) {
            throw new RecordNotFoundException("Unable to find the application with referenceNumber " + givenReferenceNumber);
        }

        CertificateApplicationMilestone lastMilestone = foundApplication.getLatestMilestone();

        StateVariable stateVariable = buildStateVariable(dto, lastMilestone.getStatusDetails());
        Status newStatus = workflowService.transition(
            new StateTransition(
                lastMilestone.getStatus(), dto.getEvent(),
                mapper.convertValue(stateVariable, new TypeReference<>() {})
            )
        );

        CertificateApplicationMilestone newMilestone = CertificateApplicationMilestone.builder()
            .status(newStatus)
            .statusDetails(MapUtils.isEmpty(stateVariable.getNewDetails()) ? null : stateVariable.getNewDetails())
            .transitionedBy(dto.getTransitionedBy())
            .transitionedDt(DateTimeHelper.getTodayInPHTDefaultFormat())
            .build();

        foundApplication.addMilestone(newMilestone);
        CertificateApplication updatedApplication = certificateApplicationRepository.save(foundApplication);

        domainEventPublisher.publish(updatedApplication);
    }

    private StateVariable buildStateVariable(
        TransitionApplicationDto dto, Map<Object, Object> currentDetails
    ) {
        var stateVariable = new StateVariable();
        stateVariable.setReferenceNumber(dto.getReferenceNumber());
        stateVariable.setCurrentDetails(MapUtils.emptyIfNull(currentDetails));

        Map<Object, Object> newDetails = switch (dto.getEvent()) {
            case LINE_MANAGER_APPROVE -> Map.of("requester_email", dto.getRequesterEmail());
            case REJECT -> Map.of("reason", dto.getReason());
            case LINE_MANAGER_REJECT -> Map.of("reason", dto.getReason(), "requester_email", dto.getRequesterEmail());
            default -> Map.of();
        };

        stateVariable.setNewDetails(newDetails);

        return stateVariable;
    }

    @Override
    public PaginatedApplicationDto getPaginatedApplications(GetFilteredApplicationDto dto) {
        if (Objects.isNull(dto)) {
            throw new IllegalArgumentException("Please provide a valid request");
        }

        return latestApplicationDetailsRepository.findAll(dto);
    }

    @Override
    public CertificateApplicationDetails getApplicationDetails(String referenceNumber) {
        if (StringUtils.isBlank(referenceNumber)) {
            throw new IllegalArgumentException("Please provide a valid reference number");
        }

        CertificateApplication foundApplication = certificateApplicationRepository.findByReferenceNumber(referenceNumber);

        if (Objects.isNull(foundApplication)) {
            throw new RecordNotFoundException("Unable to find the application with referenceNumber " + referenceNumber);
        }

        var details = CertificateApplicationDetails.builder()
            .referenceNumber(foundApplication.getReferenceNumber())
            .firstName(foundApplication.getFirstName())
            .lastName(foundApplication.getLastName())
            .middleInitial(foundApplication.getMiddleInitial())
            .hiredDate(DateTimeHelper.convertToFullDateFormat(foundApplication.getHiredDate()))
            .position(foundApplication.getPosition())
            .department(foundApplication.getDepartment().getActualName())
            .employmentStatus(foundApplication.getEmploymentStatus().getValue())
            .withCompensation(foundApplication.isWithCompensation())
            .annualCompensation(
                Objects.isNull(foundApplication.getAnnualCompensation()) ? null : NumberUtility.format(foundApplication.getAnnualCompensation())
            ).purpose(foundApplication.getPurpose().getFriendlyName())
            .additionalInfo(foundApplication.getAdditionalInfo())
            .addressee(foundApplication.getAddressee())
            .placeOfAddressee(foundApplication.getPlaceOfAddressee())
            .requestedBy(foundApplication.getRequestedBy())
            .requestedDt(DateTimeHelper.convertToFullDateTimeFormat(foundApplication.getRequestedDt()))
            .modifiedBy(foundApplication.getModifiedBy())
            .modifiedDt(DateTimeHelper.convertToFullDateTimeFormat(foundApplication.getModifiedDt()))
            .build();

        if (StringUtils.isNotBlank(foundApplication.getLineManagerEmail())) {
            details.getAdditionalInfo().put("line_manager_email", foundApplication.getLineManagerEmail());
        }

        details.getMilestones().addAll(
            foundApplication.getMilestones()
                .stream()
                .sorted(Comparator.comparing(CertificateApplicationMilestone::getId).reversed())
                .map(milestone -> CertificateApplicationMilestoneDetails.builder()
                    .status(milestone.getStatus().getFriendlyName())
                    .remarks(
                        Objects.isNull(milestone.getStatusDetails())
                            ? null
                            : milestone.getStatusDetails().getOrDefault("reason", "").toString()
                    ).transitionedBy(milestone.getTransitionedBy())
                    .transitionedDt(DateTimeHelper.convertToFullDateTimeFormat(milestone.getTransitionedDt()))
                    .build())
                .toList()
        );

        if (CollectionUtils.isNotEmpty(foundApplication.getGeneratedCertificates())) {
            GeneratedCertificate latestCertificate = foundApplication.getLatestGeneratedCertificate();
            details.setLatestCertificate(GeneratedCertificateDetails.builder()
                .fileName(latestCertificate.getFileName())
                .presignedUrl(fileRepository.generatePresignedURL(latestCertificate.getS3Key(), latestCertificate.getS3Bucket()))
                .generatedBy(latestCertificate.getGeneratedBy())
                .generatedDt(DateTimeHelper.convertToFullDateTimeFormat(latestCertificate.getGeneratedDt()))
                .build()
            );

            if (foundApplication.getGeneratedCertificates().size() > 1) {
                details.getPastCertificates().addAll(
                    foundApplication.getGeneratedCertificates()
                        .stream()
                        .sorted(Comparator.comparing(GeneratedCertificate::getVersion).reversed())
                        .skip(1)
                        .map(certificate -> GeneratedCertificateDetails.builder()
                            .fileName(certificate.getFileName())
                            .presignedUrl(fileRepository.generatePresignedURL(certificate.getS3Key(), certificate.getS3Bucket()))
                            .generatedBy(certificate.getGeneratedBy())
                            .generatedDt(DateTimeHelper.convertToFullDateTimeFormat(certificate.getGeneratedDt()))
                            .build())
                        .toList()
                );
            }
        }

        return details;
    }

    @Override
    public void updateApplication(UpdateApplicationDetails dto) {
        if (Objects.isNull(dto)) {
            throw new IllegalArgumentException("Please provide a valid request");
        }

        CertificateApplication foundApplication = certificateApplicationRepository.findByReferenceNumber(dto.getReferenceNumber());

        if (Objects.isNull(foundApplication)) {
            throw new RecordNotFoundException("Unable to find the application with referenceNumber " + dto.getReferenceNumber());
        }

        var updatedFields = new HashSet<String>();
        if (Objects.nonNull(dto.getBusinessUnit()) && !foundApplication.getBusinessUnit().equals(dto.getBusinessUnit())) {
            foundApplication.setBusinessUnit(dto.getBusinessUnit());
            updatedFields.add("Business Unit");
        }

        if (Objects.nonNull(dto.getHiredDate()) && !foundApplication.getHiredDate().equals(dto.getHiredDate())) {
            foundApplication.setHiredDate(dto.getHiredDate());
            updatedFields.add("Hired Date");
        }

        if (Objects.nonNull(dto.getEmploymentStatus()) && !foundApplication.getEmploymentStatus().equals(dto.getEmploymentStatus())) {
            foundApplication.setEmploymentStatus(dto.getEmploymentStatus());
            updatedFields.add("Employment Status");
        }

        if (StringUtils.isNotBlank(dto.getPosition()) && !foundApplication.getPosition().equalsIgnoreCase(dto.getPosition().trim())) {
            foundApplication.setPosition(dto.getPosition().trim());
            updatedFields.add("Position");
        }

        if (Objects.nonNull(dto.getDepartment()) && !foundApplication.getDepartment().equals(dto.getDepartment())) {
            foundApplication.setDepartment(dto.getDepartment());
            updatedFields.add("Department");
        }

        if (Objects.nonNull(dto.getWithCompensation()) && Objects.equals(foundApplication.isWithCompensation(), dto.getWithCompensation())) {
            foundApplication.setWithCompensation(dto.getWithCompensation());
            updatedFields.add("With Compensation");
        }

        if (foundApplication.isWithCompensation() && Objects.nonNull(dto.getAnnualCompensation())) {
            foundApplication.setAnnualCompensation(dto.getAnnualCompensation());
            updatedFields.add("Annual Compensation");
        }

        if (CollectionUtils.isEmpty(updatedFields)) {
            log.info("The application with reference number {} has no updated fields, skipping update", dto.getReferenceNumber());
            return;
        }

        foundApplication.setModifiedBy(dto.getUpdatedBy());
        foundApplication.setModifiedDt(DateTimeHelper.getTodayInPHTDefaultFormat());

        certificateApplicationRepository.save(foundApplication);
        log.info("The application with reference number {} has been updated with the following fields: {}", dto.getReferenceNumber(), String.join(", ", updatedFields));
    }
}
