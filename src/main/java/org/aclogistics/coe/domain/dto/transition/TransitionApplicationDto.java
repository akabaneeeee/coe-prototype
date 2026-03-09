package org.aclogistics.coe.domain.dto.transition;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.AssertTrue;
import java.io.Serial;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.aclogistics.coe.domain.dto.Dto;
import org.aclogistics.coe.domain.model.enumeration.Event;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Rosendo Coquilla
 */
@Data
@NoArgsConstructor
public class TransitionApplicationDto implements Dto {

    @Serial
    private static final long serialVersionUID = 3862245440321436094L;

    private String referenceNumber;
    private Event event;

    private String reason;
    private String transitionedBy = "sen"; // SecurityUtility.getUserFullName();
    private String requesterEmail = "rosendo.coquilla+line-manager@aclogistics.com.ph"; // SecurityUtility.getUserEmail();

    @JsonIgnore
    @AssertTrue(message = "The rejection reason must be provided when rejecting an application")
    private boolean isReasonForRejectionProvided() {
        if (Event.REJECT.equals(event) || Event.LINE_MANAGER_REJECT.equals(event)) {
            return StringUtils.isNotBlank(reason);
        }

        return true;
    }
}
