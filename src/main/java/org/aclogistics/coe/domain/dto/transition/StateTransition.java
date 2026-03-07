package org.aclogistics.coe.domain.dto.transition;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.aclogistics.coe.domain.enumeration.Event;
import org.aclogistics.coe.domain.enumeration.Status;

/**
 * @author Rosendo Coquilla
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StateTransition {

    private Status currentStatus;
    private Event event;
    private Map<Object, Object> variables;
}
