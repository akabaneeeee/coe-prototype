package org.aclogistics.coe.domain.dto.filter;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.io.Serial;
import lombok.Data;
import org.aclogistics.coe.domain.dto.Dto;
import org.aclogistics.coe.domain.model.enumeration.Department;
import org.aclogistics.coe.domain.model.enumeration.EmploymentStatus;

/**
 * @author Rosendo Coquilla
 */
@Data
@JsonNaming(SnakeCaseStrategy.class)
public class EmploymentStatusFilter implements Dto {

    @Serial
    private static final long serialVersionUID = -2454296030095979464L;

    private EmploymentStatus value;
    private String name;
}
