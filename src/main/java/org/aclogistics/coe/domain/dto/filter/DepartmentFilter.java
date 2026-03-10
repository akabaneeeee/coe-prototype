package org.aclogistics.coe.domain.dto.filter;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.io.Serial;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.aclogistics.coe.domain.dto.Dto;
import org.aclogistics.coe.domain.model.enumeration.Beneficiary;
import org.aclogistics.coe.domain.model.enumeration.Department;

/**
 * @author Rosendo Coquilla
 */
@Data
@AllArgsConstructor
@JsonNaming(SnakeCaseStrategy.class)
public class DepartmentFilter implements Dto {

    @Serial
    private static final long serialVersionUID = -2454296030095979464L;

    private Department value;
    private String name;
}
