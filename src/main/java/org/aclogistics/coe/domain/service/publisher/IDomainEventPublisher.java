package org.aclogistics.coe.domain.service.publisher;

import org.aclogistics.coe.domain.model.CertificateApplication;

/**
 * @author Rosendo Coquilla
 */
public interface IDomainEventPublisher {

    void publish(CertificateApplication application);
}
