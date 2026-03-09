package org.aclogistics.coe.domain.port;

import org.aclogistics.coe.domain.dto.transition.StateTransition;
import org.aclogistics.coe.domain.model.enumeration.Status;

/**
 * @author Rosendo Coquilla
 */
public interface IWorkflowService {

    Status getInitialState();
    Status transition(StateTransition transition);
}
