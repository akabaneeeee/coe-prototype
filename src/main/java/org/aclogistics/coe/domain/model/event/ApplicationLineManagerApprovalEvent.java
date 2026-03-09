package org.aclogistics.coe.domain.model.event;

import lombok.Getter;
import org.aclogistics.coe.domain.model.CertificateApplication;
import org.springframework.context.ApplicationEvent;

/**
 * @author Rosendo Coquilla
 */
@Getter
public class ApplicationLineManagerApprovalEvent extends ApplicationEvent {

    private final CertificateApplication application;

    public ApplicationLineManagerApprovalEvent(Object source, CertificateApplication application) {
        super(source);
        this.application = application;
    }
}
