package org.aclogistics.coe.infrastructure.statemachine;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aclogistics.coe.domain.dto.StateTransition;
import org.aclogistics.coe.domain.enumeration.Event;
import org.aclogistics.coe.domain.enumeration.Status;
import org.aclogistics.coe.domain.exception.StatusTransitionFailedException;
import org.aclogistics.coe.domain.exception.StatusVerificationFailedException;
import org.aclogistics.coe.domain.port.IWorkflowService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachineEventResult.ResultType;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultExtendedState;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * @author Rosendo Coquilla
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class StateMachineService implements IWorkflowService {

    private final StateMachineFactory<Status, Event> stateMachineFactory;

    @Override
    public Status getInitialState() {
        StateMachine<Status, Event> stateMachine = stateMachineFactory.getStateMachine(UUID.randomUUID().toString());
        stateMachine.startReactively().block();

        var initialState = stateMachine.getInitialState().getId();
        stateMachine.stopReactively().block();

        return initialState;
    }

    @Override
    public Status transition(StateTransition transition) {
        var currentStatus = transition.getCurrentStatus();
        var event = transition.getEvent();

        final var newStateMachine = stateMachineFactory.getStateMachine(UUID.randomUUID().toString());

        DefaultExtendedState extendedState = new DefaultExtendedState(new HashMap<>());
        transition.getVariables()
            .forEach((k, v) -> extendedState.getVariables().put(k, v));

        newStateMachine.getStateMachineAccessor()
            .doWithAllRegions(access ->
                access.resetStateMachineReactively(
                    new DefaultStateMachineContext<>(
                        currentStatus, null, null, extendedState
                    )
                ).doOnError(e -> log.error("Error resetting state machine", e)
                ).block()
            );

        log.info("Initializing state machine for current status: {}", currentStatus);
        newStateMachine.startReactively().block();

        log.info("Transitioning current status ({}) using event: {}", currentStatus, event);
        var result = newStateMachine.sendEvent(
            Mono.just(MessageBuilder.withPayload(transition.getEvent()).build())
        ).blockLast();

        if (Objects.isNull(result) || ResultType.DENIED.equals(result.getResultType())) {
            newStateMachine.stopReactively().block();
            throw new StatusTransitionFailedException(currentStatus.name(), event.name());
        }

        var error = (String) newStateMachine.getExtendedState()
            .getVariables()
            .getOrDefault("error", "");

        if (StringUtils.isNotBlank(error)) {
            newStateMachine.stopReactively().block();
            throw new StatusVerificationFailedException(error);
        }

        log.info(
            "Successfully transitioned the status from {} to {}",
            currentStatus, newStateMachine.getState().getId()
        );
        newStateMachine.stopReactively().block();

        return newStateMachine.getState().getId();
    }
}
