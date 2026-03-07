package org.aclogistics.coe.infrastructure.configuration.statemachine;

import java.util.EnumSet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aclogistics.coe.domain.enumeration.Event;
import org.aclogistics.coe.domain.enumeration.Status;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

/**
 * @author Rosendo Coquilla
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableStateMachineFactory(name = "stateMachineFactory")
public class StateMachineConfiguration extends StateMachineConfigurerAdapter<Status, Event> {

    @Override
    public void configure(StateMachineStateConfigurer<Status, Event> states) throws Exception {
        states.withStates()
            .initial(Status.REQUESTED)
            .states(EnumSet.allOf(Status.class))
            .end(Status.CERTIFICATE_READY)
            .end(Status.REJECTED);
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<Status, Event> transitions) throws Exception {
        transitions
            .withExternal()
                .source(Status.REQUESTED).target(Status.REJECTED)
                .event(Event.REJECT)
            .and()
            .withExternal()
                .source(Status.REQUESTED).target(Status.LINE_MANAGER_FOR_APPROVAL)
                .event(Event.FOR_APPROVAL)
            .and()
            .withExternal()
                .source(Status.REQUESTED).target(Status.HR_PROCESSING)
                .event(Event.ACCEPT)
            .and()
            .withExternal()
                .source(Status.LINE_MANAGER_FOR_APPROVAL).target(Status.LINE_MANAGER_APPROVED)
                .event(Event.LINE_MANAGER_APPROVE)
            .and()
            .withExternal()
                .source(Status.LINE_MANAGER_FOR_APPROVAL).target(Status.REJECTED)
                .event(Event.LINE_MANAGER_REJECT)
            .and()
            .withExternal()
                .source(Status.LINE_MANAGER_APPROVED).target(Status.HR_PROCESSING)
                .event(Event.ACCEPT)
            .and()
            .withExternal()
                .source(Status.HR_PROCESSING).target(Status.HR_APPROVED)
                .event(Event.HR_APPROVE)
            .and()
            .withExternal()
                .source(Status.HR_APPROVED).target(Status.CERTIFICATE_READY)
                .event(Event.CERTIFICATE_GENERATED);
    }
}
