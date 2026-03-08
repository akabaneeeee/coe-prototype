package org.aclogistics.coe.domain.model;

import java.io.Serial;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Rosendo Coquilla
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GeneratedCertificate implements Model {

    @Serial
    private static final long serialVersionUID = -4899579634918617216L;

    private Integer id;
    private Short version;
    private String fileName;
    private String s3Key;
    private String s3Bucket;
    private String generatedBy;
    private LocalDateTime generatedDt;
}
