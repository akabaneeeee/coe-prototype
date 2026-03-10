package org.aclogistics.coe.domain.dto.filter;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.io.Serial;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.aclogistics.coe.domain.dto.Dto;
import org.aclogistics.coe.domain.model.enumeration.BusinessUnit;

/**
 * @author Rosendo Coquilla
 */
@Data
@AllArgsConstructor
@JsonNaming(SnakeCaseStrategy.class)
public class BusinessUnitFilter implements Dto {

    @Serial
    private static final long serialVersionUID = -6013159273584259922L;

    private BusinessUnit value;
    private String name;
}
