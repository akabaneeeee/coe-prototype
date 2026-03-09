package org.aclogistics.coe.domain.dto.application.filtered;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.io.Serial;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.aclogistics.coe.domain.dto.Dto;

/**
 * @author Rosendo Coquilla
 */
@Data
@Builder
@AllArgsConstructor
@JsonNaming(SnakeCaseStrategy.class)
public class CertificateApplicationDto implements Dto {

    @Serial
    private static final long serialVersionUID = 1973079741173182154L;

    private String referenceNumber;
    private String fullName;
    private String businessUnit;
    private String department;
    private String purpose;
    private String lineManagerFullName;
    private String lineManagerEmail;
    private String status;
    private String transitionedBy;
    private String transitionedDt;
}
