package org.aclogistics.coe.domain.service.listener;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aclogistics.coe.domain.dto.TemplateDetails;
import org.aclogistics.coe.domain.email.EmailRequestDto;
import org.aclogistics.coe.domain.event.ApplicationRejectedEvent;
import org.aclogistics.coe.domain.model.CertificateApplication;
import org.aclogistics.coe.domain.model.CertificateApplicationMilestone;
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
public class ApplicationRejectedEventListener {

    private final IHtmlTemplateRenderer htmlTemplateRenderer;
    private final IEmailNotificationSender emailNotificationSender;

    @Async
    @EventListener
    public void onApplicationRejectedEvent(ApplicationRejectedEvent event) {
        if (Objects.isNull(event)) {
            log.info("The received ApplicationRejectedEvent is null, skipping...");
            return;
        }

        CertificateApplication application = event.getApplication();
        CertificateApplicationMilestone latestMilestone = application.getLatestMilestone();

        Map<String, Object> templateVariables = new HashMap<>();
        templateVariables.put("requestor_name", application.getRequestedBy());
        templateVariables.put("reference_number", application.getReferenceNumber());
        templateVariables.put("remarks", latestMilestone.getRejectionReason());
        templateVariables.put("rejected_by", latestMilestone.getTransitionedBy());
        templateVariables.put("rejected_date", DateTimeHelper.convertToFullDateTimeFormat(latestMilestone.getTransitionedDt()));
        templateVariables.put("hr_system_link", "http://localhost:7005/");

        String htmlContent = htmlTemplateRenderer.render(new TemplateDetails(templateVariables, "email/rejected.html"));

        emailNotificationSender.send(
            EmailRequestDto.builder()
                .source("HR System <hr.no-reply@aclogistics.com.ph>")
                .to(Set.of(application.getRequesterEmail()))
                .subject("[COE] Application Rejected")
                .htmlContent(htmlContent)
                .build()
        );
    }
}
