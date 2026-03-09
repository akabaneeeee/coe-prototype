package org.aclogistics.coe.domain.port;

import org.aclogistics.coe.domain.dto.email.EmailRequestDto;

/**
 * @author Rosendo Coquilla
 */
public interface IEmailNotificationSender {

    void send(EmailRequestDto request);
}
