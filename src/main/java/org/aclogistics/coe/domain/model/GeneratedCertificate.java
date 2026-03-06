package org.aclogistics.coe.domain.model;

import java.io.Serial;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Rosendo Coquilla
 */
@Data
@NoArgsConstructor
public class GeneratedCertificate implements Model {

    @Serial
    private static final long serialVersionUID = -4899579634918617216L;

    private Integer id;
    private Integer version;
    private String s3Key;
    private String s3Bucket;
    private String transitionedBy;
    private LocalDateTime transitionedDt;
}
