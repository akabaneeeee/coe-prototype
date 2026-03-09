package org.aclogistics.coe.domain.service.listener;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aclogistics.coe.domain.dto.TemplateDetails;
import org.aclogistics.coe.domain.dto.email.EmailRequestDto;
import org.aclogistics.coe.domain.model.event.EmploymentCertificateGeneratedEvent;
import org.aclogistics.coe.domain.model.CertificateApplication;
import org.aclogistics.coe.domain.port.IEmailNotificationSender;
import org.aclogistics.coe.domain.port.IHtmlTemplateRenderer;
import org.aclogistics.coe.domain.utility.DateTimeHelper;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author Rosendo Coquilla
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class EmploymentCertificateGeneratedEventListener {

    private final IHtmlTemplateRenderer htmlTemplateRenderer;
    private final IEmailNotificationSender emailNotificationSender;

    @Async
    @EventListener
    public void onEmploymentCertificateGeneratedEvent(EmploymentCertificateGeneratedEvent event) {
        if (Objects.isNull(event)) {
            log.info("The received ApplicationLineManagerApprovalEvent is null, skipping...");
            return;
        }

        CertificateApplication application = event.getApplication();

        Map<String, Object> templateVariables = new HashMap<>();
        templateVariables.put("requestor_name", application.getRequestedBy());
        templateVariables.put("reference_number", application.getReferenceNumber());
        templateVariables.put("purpose", application.getPurpose().getFriendlyName());
        templateVariables.put("requested_date", DateTimeHelper.convertToFullDateTimeFormat(application.getRequestedDt()));
        templateVariables.put("hr_system_link", "http://localhost:7005/");

        String htmlContent = htmlTemplateRenderer.render(new TemplateDetails(templateVariables, "email/certificate-ready.html"));

        emailNotificationSender.send(
            EmailRequestDto.builder()
                .source("HR System <hr.no-reply@aclogistics.com.ph>")
                .to(Set.of(application.getRequesterEmail()))
                .subject("[COE] Your certificate is ready for download")
                .htmlContent(htmlContent)
                .build()
        );
    }
}
