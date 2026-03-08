package org.aclogistics.coe.infrastructure.configuration.statemachine.guard;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aclogistics.coe.domain.enumeration.Event;
import org.aclogistics.coe.domain.enumeration.Status;
import org.aclogistics.coe.domain.model.CertificateApplication;
import org.aclogistics.coe.domain.port.ICertificateApplicationRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.guard.Guard;
import org.springframework.stereotype.Component;

/**
 * @author Rosendo Coquilla
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LineManagerNotRequiredGuard implements Guard<Status, Event> {

    private final ICertificateApplicationRepository repository;

    @Override
    public boolean evaluate(StateContext<Status, Event> context) {
        var variables = context.getExtendedState().getVariables();
        var referenceNumber = Objects.toString(variables.get("reference_number"));

        CertificateApplication application = repository.findByReferenceNumber(referenceNumber);
        String lineManager = application.getLineManagerEmailIfExists();

        if (StringUtils.isNotBlank(lineManager)) {
            log.info("This application has line manager email, denying transition...");

            variables.put("error", "The application need a line manager approval before HR acceptance");
            return false;
        }

        return true;
    }
}
