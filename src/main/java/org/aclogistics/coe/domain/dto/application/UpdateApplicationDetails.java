package org.aclogistics.coe.domain.dto.application;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.AssertTrue;
import java.io.Serial;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.aclogistics.coe.domain.dto.Dto;
import org.aclogistics.coe.domain.model.enumeration.BusinessUnit;
import org.aclogistics.coe.domain.model.enumeration.Department;
import org.aclogistics.coe.domain.model.enumeration.EmploymentStatus;

/**
 * @author Rosendo Coquilla
 */
@Data
@NoArgsConstructor
@JsonNaming(SnakeCaseStrategy.class)
public class UpdateApplicationDetails implements Dto {

    @Serial
    private static final long serialVersionUID = 3111644753322643976L;

    private String referenceNumber;

    private BusinessUnit businessUnit;

    @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate hiredDate;

    private EmploymentStatus employmentStatus;

    private String position;

    private Department department;

    private Boolean withCompensation;

    private BigDecimal annualCompensation;

    private String updatedBy = "sen"; //SecurityUtility.getUserFullName();

    @JsonIgnore
    @AssertTrue(message = "You must provide the annual compensation")
    public boolean isWithAnnualCompensation() {
        if (Objects.isNull(this.withCompensation)) {
            return true;
        }

        if (!this.withCompensation) {
            return true;
        }

        if (annualCompensation.compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }

        int integerDigits = annualCompensation.precision() - annualCompensation.scale();
        int fractionDigits = annualCompensation.scale();

        return integerDigits <= 9 && fractionDigits <= 2;
    }
}
