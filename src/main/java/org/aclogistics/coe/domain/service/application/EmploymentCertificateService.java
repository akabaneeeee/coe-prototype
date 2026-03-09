package org.aclogistics.coe.domain.service.application;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aclogistics.coe.domain.dto.application.ApplyCertificateDto;
import org.aclogistics.coe.domain.dto.application.additionalinfo.AdditionalInfo;
import org.aclogistics.coe.domain.dto.application.additionalinfo.HasLineManagerEmail;
import org.aclogistics.coe.domain.dto.transition.StateTransition;
import org.aclogistics.coe.domain.dto.transition.StateVariable;
import org.aclogistics.coe.domain.dto.transition.TransitionApplicationDto;
import org.aclogistics.coe.domain.enumeration.Status;
import org.aclogistics.coe.domain.exception.RecordNotFoundException;
import org.aclogistics.coe.domain.model.CertificateApplication;
import org.aclogistics.coe.domain.model.CertificateApplicationMilestone;
import org.aclogistics.coe.domain.port.ICertificateApplicationRepository;
import org.aclogistics.coe.domain.port.IWorkflowService;
import org.aclogistics.coe.domain.service.idgenerator.IdGenerator;
import org.aclogistics.coe.domain.service.publisher.IDomainEventPublisher;
import org.aclogistics.coe.domain.utility.DateTimeHelper;
import org.aclogistics.coe.infrastructure.jpa.exception.DuplicateReferenceNumberException;
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
}
