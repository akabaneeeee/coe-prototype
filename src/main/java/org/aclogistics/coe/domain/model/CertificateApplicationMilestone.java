package org.aclogistics.coe.domain.model;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.aclogistics.coe.domain.model.enumeration.Status;
import org.apache.commons.collections4.MapUtils;

/**
 * @author Rosendo Coquilla
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CertificateApplicationMilestone implements Model {

    @Serial
    private static final long serialVersionUID = -6063963105284753567L;

    private Integer id;
    private Status status;
    private Map<Object, Object> statusDetails;
    private String transitionedBy;
    private LocalDateTime transitionedDt;

    public CertificateApplicationMilestone(String requestedBy, LocalDateTime requestedDt) {
        this.status = Status.REQUESTED;
        this.transitionedBy = requestedBy;
        this.transitionedDt = requestedDt;
    }

    /**
     * Used to get the rejection reason from the status details
     *
     * @return reason - the rejection reason
     */
    public String getRejectionReason() {
        return MapUtils.isEmpty(this.statusDetails)
            ? null : Objects.toString(this.statusDetails.get("reason"));
    }
}
