package org.aclogistics.coe.domain.dto.application.additionalinfo;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import lombok.Data;
import org.aclogistics.coe.domain.enumeration.Beneficiary;

/**
 * @author Rosendo Coquilla
 */
@Data
@JsonTypeName("SCHOOL_REQUIREMENTS")
@JsonNaming(SnakeCaseStrategy.class)
public class SchoolRequirementsDetails implements AdditionalInfo {

    @Serial
    private static final long serialVersionUID = 1250157949607728884L;

    @NotNull(message = "Please provide the beneficiary of your certificate")
    private Beneficiary beneficiary;
}
