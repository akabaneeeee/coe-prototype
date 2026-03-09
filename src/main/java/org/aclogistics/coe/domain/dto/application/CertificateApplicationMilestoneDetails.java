package org.aclogistics.coe.domain.dto.application;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
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
@JsonInclude(Include.NON_NULL)
@JsonNaming(SnakeCaseStrategy.class)
public class CertificateApplicationMilestoneDetails implements Dto {

    @Serial
    private static final long serialVersionUID = 710151720130032104L;

    private String status;
    private String remarks;
    private String transitionedBy;
    private String transitionedDt;
}
