package org.aclogistics.coe.domain.dto.transition;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.Map;
import lombok.Data;

/**
 * @author Rosendo Coquilla
 */
@Data
@JsonNaming(SnakeCaseStrategy.class)
public class StateVariable {

    private String referenceNumber;
    private Map<Object, Object> currentDetails;
    private Map<Object, Object> newDetails;
}
