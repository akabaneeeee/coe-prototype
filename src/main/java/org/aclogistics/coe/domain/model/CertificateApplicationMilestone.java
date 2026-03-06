package org.aclogistics.coe.domain.model;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.Map;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Rosendo Coquilla
 */
@Data
@NoArgsConstructor
public class CertificateApplicationMilestone implements Model {

    @Serial
    private static final long serialVersionUID = -6063963105284753567L;

    private Integer id;
    private String status;
    private Map<String, String> statusDetails;
    private String transitionedBy;
    private LocalDateTime transitionedDt;
}
