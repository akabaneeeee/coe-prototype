package org.aclogistics.coe.infrastructure.configuration.statemachine.guard;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
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
public class LineManagerApprovalGuard implements Guard<Status, Event> {

    private final ICertificateApplicationRepository repository;
    private final ObjectMapper mapper;

    @Override
    public boolean evaluate(StateContext<Status, Event> context) {
        var variables = context.getExtendedState().getVariables();
        var referenceNumber = Objects.toString(variables.get("reference_number"));
        var newDetails = mapper.convertValue(
            variables.get("new_details"),
            new TypeReference<Map<String, String>>() {}
        );

        CertificateApplication application = repository.findByReferenceNumber(referenceNumber);
        String lineManager = application.getLineManagerEmailIfExists();

        if (StringUtils.isBlank(lineManager)) {
            log.info("This application has no line manager email, denying transition...");

            variables.put("error", "The application does not indicate the requestor's line manager");
            return false;
        }

        if (Objects.equals(Objects.toString(newDetails.get("requester_email")), lineManager)) {
            log.info("The tagged line manager email matches the requestor of this transition");
            return true;
        }

        variables.put("error", "You are not tagged as the line manager of this application");
        return false;
    }
}
