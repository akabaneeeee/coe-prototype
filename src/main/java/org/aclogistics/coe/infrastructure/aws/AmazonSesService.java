package org.aclogistics.coe.infrastructure.aws;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aclogistics.coe.domain.email.EmailRequestDto;
import org.aclogistics.coe.domain.port.IEmailNotificationSender;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.Content;
import software.amazon.awssdk.services.ses.model.SendEmailRequest;

/**
 * @author Rosendo Coquilla
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AmazonSesService implements IEmailNotificationSender {

    private final SesClient sesClient;

    @Override
    public void send(EmailRequestDto request) {
        if (Objects.isNull(request)) {
            throw new IllegalArgumentException("Request is null");
        }

        SendEmailRequest emailRequest = SendEmailRequest.builder()
            .destination(builder -> {
                builder.toAddresses(request.getTo());
                builder.ccAddresses(request.getCc());
                builder.bccAddresses(request.getBcc());
            }).source(request.getSource())
            .message(builder -> {
                builder.subject(buildContent(request.getSubject()));
                builder.body(body -> body.html(buildContent(request.getHtmlContent())));
            }).build();

        try {
            log.info("Attempting to send email to: {}", String.join(", ", request.getTo()));
            sesClient.sendEmail(emailRequest);
            log.info("Email successfully sent!");
        } catch (Exception e) {
            log.error("Unexpected error occurred while sending email: ", e);
        }
    }

    private Content buildContent(String data) {
        return Content.builder()
            .data(data)
            .charset("UTF-8")
            .build();
    }
}
