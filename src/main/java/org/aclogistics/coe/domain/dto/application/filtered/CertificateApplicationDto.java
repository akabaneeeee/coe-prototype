package org.aclogistics.coe.domain.dto.application.filtered;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.io.Serial;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.aclogistics.coe.domain.dto.Dto;
import org.springframework.hateoas.RepresentationModel;

/**
 * @author Rosendo Coquilla
 */
@Data
@Builder
@AllArgsConstructor
@JsonNaming(SnakeCaseStrategy.class)
@EqualsAndHashCode(callSuper = false)
public class CertificateApplicationDto extends RepresentationModel<CertificateApplicationDto> implements Dto {

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
