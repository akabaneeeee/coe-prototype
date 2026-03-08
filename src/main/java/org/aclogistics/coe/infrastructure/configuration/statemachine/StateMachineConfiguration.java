package org.aclogistics.coe.infrastructure.configuration.statemachine;

import java.util.EnumSet;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aclogistics.coe.domain.enumeration.Event;
import org.aclogistics.coe.domain.enumeration.Status;
import org.aclogistics.coe.infrastructure.configuration.statemachine.guard.LineManagerApprovalGuard;
import org.aclogistics.coe.infrastructure.configuration.statemachine.guard.LineManagerNotRequiredGuard;
import org.aclogistics.coe.infrastructure.configuration.statemachine.guard.LineManagerRequiredGuard;
import org.aclogistics.coe.infrastructure.configuration.statemachine.guard.RejectionReasonGuard;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.guard.Guard;

/**
 * @author Rosendo Coquilla
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableStateMachineFactory(name = "stateMachineFactory")
public class StateMachineConfiguration extends StateMachineConfigurerAdapter<Status, Event> {

    private final LineManagerRequiredGuard lineManagerRequiredGuard;
    private final LineManagerNotRequiredGuard lineManagerNotRequiredGuard;
    private final LineManagerApprovalGuard lineManagerApprovalGuard;
    private final RejectionReasonGuard rejectionReasonGuard;

    @Override
    public void configure(StateMachineStateConfigurer<Status, Event> states) throws Exception {
        states.withStates()
            .initial(Status.REQUESTED)
            .states(EnumSet.allOf(Status.class))
            .end(Status.CERTIFICATE_READY)
            .end(Status.REJECTED);
    }

    /**
     * Happy Paths:
     * <ul>
     *     <li>With Line Manager Approval:</li>
     *         <ul>
     *             <li>Request -> Line Manager for Approval -> Line Manager Approved -> HR Processing -> Certificate Ready</li>
     *         </ul>
     *     <li>Without Line Manager Approval:</li>
     *         <ul>
     *             <li>Request -> HR Processing -> HR Approved -> Certificate Ready</li>
     *         </ul>
     * </ul>
     *
     * Rejection Paths:
     * <ul>
     *     <li>Request -> Rejected</li>
     *     <li>Request -> Line Manager for Approval -> Line Manager Rejected</li>
     * </ul>
     *
     * @param transitions - the transitions
     * @throws Exception - the generic exception
     */
    @Override
    public void configure(StateMachineTransitionConfigurer<Status, Event> transitions) throws Exception {
        transitions
            .withExternal()
                .source(Status.REQUESTED).target(Status.REJECTED)
                .guard(rejectionReasonGuard)
                .event(Event.REJECT)
            .and()
            .withExternal()
                .source(Status.REQUESTED).target(Status.LINE_MANAGER_FOR_APPROVAL)
                .guard(lineManagerRequiredGuard)
                .event(Event.FOR_APPROVAL)
            .and()
            .withExternal()
                .source(Status.REQUESTED).target(Status.HR_PROCESSING)
                .guard(lineManagerNotRequiredGuard)
                .event(Event.ACCEPT)
            .and()
            .withExternal()
                .source(Status.LINE_MANAGER_FOR_APPROVAL).target(Status.LINE_MANAGER_APPROVED)
                .guard(lineManagerApprovalGuard)
                .event(Event.LINE_MANAGER_APPROVE)
            .and()
            .withExternal()
                .source(Status.LINE_MANAGER_FOR_APPROVAL).target(Status.REJECTED)
                .guard(this.buildCompositeGuard(List.of(lineManagerApprovalGuard, rejectionReasonGuard)))
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

    private Guard<Status, Event> buildCompositeGuard(List<Guard<Status, Event>> guards) {
        Guard<Status, Event> combined = guards.getFirst();

        for (int i = 1; i < guards.size(); i++) {
            final Guard<Status, Event> next = guards.get(i);
            final Guard<Status, Event> current = combined;

            combined = (StateContext<Status, Event> ctx) -> current.evaluate(ctx) && next.evaluate(ctx);
        }

        return combined;
    }
}
