package org.aclogistics.coe.infrastructure.configuration.statemachine.guard;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aclogistics.coe.domain.enumeration.Event;
import org.aclogistics.coe.domain.enumeration.Status;
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
public class RejectionReasonGuard  implements Guard<Status, Event> {

    private final ObjectMapper mapper;

    @Override
    public boolean evaluate(StateContext<Status, Event> context) {
        var variables = context.getExtendedState().getVariables();
        var newDetails = mapper.convertValue(
            variables.get("new_details"),
            new TypeReference<Map<String, String>>() {}
        );

        if (StringUtils.isBlank(Objects.toString(newDetails.get("reason")))) {
            log.info("The application is about to be rejected but no reason was provided, denying transition...");

            variables.put("error", "Please provide a reason on why you are rejecting this application");
            return false;
        }

        return true;
    }
}
