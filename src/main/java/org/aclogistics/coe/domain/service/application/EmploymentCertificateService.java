package org.aclogistics.coe.domain.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aclogistics.coe.domain.dto.application.ApplyCertificateDto;
import org.aclogistics.coe.domain.dto.transition.StateTransition;
import org.aclogistics.coe.domain.dto.transition.StateVariable;
import org.aclogistics.coe.domain.dto.transition.TransitionApplicationDto;
import org.aclogistics.coe.domain.enumeration.BusinessUnit;
import org.aclogistics.coe.domain.enumeration.Status;
import org.aclogistics.coe.domain.exception.RecordNotFoundException;
import org.aclogistics.coe.domain.model.CertificateApplication;
import org.aclogistics.coe.domain.model.CertificateApplicationMilestone;
import org.aclogistics.coe.domain.port.ICertificateApplicationRepository;
import org.aclogistics.coe.domain.port.IHtmlTemplateRenderer;
import org.aclogistics.coe.domain.port.IPdfRendererService;
import org.aclogistics.coe.domain.port.IWatermarkService;
import org.aclogistics.coe.domain.port.IWorkflowService;
import org.aclogistics.coe.domain.service.idgenerator.IdGenerator;
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
    private final IHtmlTemplateRenderer htmlTemplateRenderer;
    private final IPdfRendererService pdfGeneratorService;
    private final IWatermarkService watermarkService;
    private final ObjectMapper mapper;
    private final IWorkflowService workflowService;

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
            .additionalInfo(mapper.convertValue(dto.getAdditionalInfo(), TYPE_REFERENCE))
            .addressee(dto.getAddressee().trim())
            .placeOfAddressee(StringUtils.defaultIfBlank(dto.getPlaceOfAddressee(), null))
            .requestedBy(dto.getRequestedBy())
            .requestedDt(now)
            .build();
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

        CertificateApplicationMilestone lastMilestone = foundApplication.getLastMilestone();

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
        certificateApplicationRepository.save(foundApplication);
    }

    @Override
    public void generateCertificate() {
        String htmlContent = htmlTemplateRenderer.render();
        byte[] originalPdfBytes = pdfGeneratorService.generate(htmlContent);

        try {
            Path outputPath = Path.of("src/main/resources/generated-coe.pdf");
            Files.createDirectories(outputPath.getParent());

            byte[] watermarkedPdfBytes = watermarkService.addWatermark(originalPdfBytes, BusinessUnit.AMC);
            Files.write(outputPath, watermarkedPdfBytes);
        } catch (IOException ex) {
            log.error("Error: ", ex);
        }
    }

    private StateVariable buildStateVariable(
        TransitionApplicationDto dto, Map<Object, Object> currentDetails
    ) {
        var stateVariable = new StateVariable();
        stateVariable.setReferenceNumber(dto.getReferenceNumber());
        stateVariable.setCurrentDetails(MapUtils.emptyIfNull(currentDetails));

        var newDetails = new HashMap<>();
        switch (dto.getEvent()) {
            case LINE_MANAGER_APPROVE -> newDetails.put("requester_email", dto.getRequesterEmail());
            case REJECT -> newDetails.put("reason", dto.getReason());
            case LINE_MANAGER_REJECT -> newDetails.putAll(Map.of("reason", dto.getReason(), "requester_email", dto.getRequesterEmail()));
            default -> newDetails = new HashMap<>();
        }

        stateVariable.setNewDetails(newDetails);

        return stateVariable;
    }
}
