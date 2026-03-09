package org.aclogistics.coe.domain.service.publisher;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aclogistics.coe.domain.model.event.ApplicationHrApprovedEvent;
import org.aclogistics.coe.domain.model.event.ApplicationLineManagerApprovalEvent;
import org.aclogistics.coe.domain.model.event.ApplicationRejectedEvent;
import org.aclogistics.coe.domain.model.event.EmploymentCertificateGeneratedEvent;
import org.aclogistics.coe.domain.model.CertificateApplication;
import org.aclogistics.coe.domain.model.CertificateApplicationMilestone;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

/**
 * @author Rosendo Coquilla
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DomainEventPublisher implements IDomainEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;
    private final ObjectMapper mapper;

    @Override
    public void publish(CertificateApplication application) {
        if (Objects.isNull(application)) {
            log.warn("The provided certificate application is null, skipping event publication...");
            return;
        }

        CertificateApplicationMilestone latestMilestone = application.getLatestMilestone();
        log.info("The application's latest milestone status is {}", latestMilestone.getStatus());
        switch (latestMilestone.getStatus()) {
            case REJECTED -> handleRejectedEvent(application);
            case PENDING_LINE_MANAGER_APPROVAL -> handleLineManagerForApprovalEvent(application);
            case HR_APPROVED -> handleHrApprovedEvent(application);
            case CERTIFICATE_READY -> handleCertificateReadyEvent(application);
            default -> log.info("The application's latest milestone status is {}, skipping event publication...", latestMilestone.getStatus());
        }
    }

    private void handleRejectedEvent(CertificateApplication application) {
        log.info("Publishing ApplicationRejectedEvent for application with reference number {}", application.getReferenceNumber());

        applicationEventPublisher.publishEvent(new ApplicationRejectedEvent(this, application));
    }

    private void handleLineManagerForApprovalEvent(CertificateApplication application) {
        log.info("Publishing ApplicationLineManagerApprovalEvent for application with reference number {}", application.getReferenceNumber());

        applicationEventPublisher.publishEvent(
            new ApplicationLineManagerApprovalEvent(this, application)
        );
    }

    private  void handleHrApprovedEvent(CertificateApplication application) {
        log.info("Publishing ApplicationHrApprovedEvent for application with reference number {}", application.getReferenceNumber());

        applicationEventPublisher.publishEvent(new ApplicationHrApprovedEvent(this, application));
    }

    private void handleCertificateReadyEvent(CertificateApplication application) {
        log.info("Publishing EmploymentCertificateGeneratedEvent for application with reference number {}", application.getReferenceNumber());

        applicationEventPublisher.publishEvent(
            new EmploymentCertificateGeneratedEvent(this, application)
        );
    }
}
