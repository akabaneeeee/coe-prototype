package org.aclogistics.coe.domain.service.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aclogistics.coe.domain.dto.TemplateDetails;
import org.aclogistics.coe.domain.dto.application.LineManagerDetails;
import org.aclogistics.coe.domain.email.EmailRequestDto;
import org.aclogistics.coe.domain.event.ApplicationLineManagerApprovalEvent;
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
public class ApplicationLineManagerApprovalListener {

    private final ObjectMapper mapper;
    private final IHtmlTemplateRenderer htmlTemplateRenderer;
    private final IEmailNotificationSender emailNotificationSender;

    @Async
    @EventListener
    public void onApplicationLineManagerApprovalEvent(ApplicationLineManagerApprovalEvent event) {
        if (Objects.isNull(event)) {
            log.info("The received ApplicationLineManagerApprovalEvent is null, skipping...");
            return;
        }

        CertificateApplication application = event.getApplication();
        LineManagerDetails lineManagerDetails = mapper.convertValue(application.getAdditionalInfo(), LineManagerDetails.class);

        Map<String, Object> templateVariables = new HashMap<>();
        templateVariables.put("approver_name", lineManagerDetails.firstName() + " " + lineManagerDetails.lastName());
        templateVariables.put("reference_number", application.getReferenceNumber());
        templateVariables.put("purpose", application.getPurpose().getFriendlyName());
        templateVariables.put("requested_by", application.getRequestedBy());
        templateVariables.put("requested_date", DateTimeHelper.convertToFullDateTimeFormat(application.getRequestedDt()));
        templateVariables.put("hr_system_link", "http://localhost:7005/");

        String htmlContent = htmlTemplateRenderer.render(new TemplateDetails(templateVariables, "email/line-manager-for-approval.html"));

        emailNotificationSender.send(
            EmailRequestDto.builder()
                .source("HR System <hr.no-reply@aclogistics.com.ph>")
                .to(Set.of(lineManagerDetails.email()))
                .subject("[COE] Application %s requires your approval".formatted(application.getReferenceNumber()))
                .htmlContent(htmlContent)
                .build()
        );
    }
}
