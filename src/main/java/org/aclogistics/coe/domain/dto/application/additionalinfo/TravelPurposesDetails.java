package org.aclogistics.coe.domain.dto.application.additionalinfo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import java.time.LocalDate;
import lombok.Data;

/**
 * @author Rosendo Coquilla
 */
@Data
@JsonTypeName("TRAVEL_PURPOSES")
@JsonNaming(SnakeCaseStrategy.class)
public class TravelPurposesDetails implements AdditionalInfo {

    @Serial
    private static final long serialVersionUID = 1250157949607728884L;

    @NotNull(message = "Please provide your date of travel")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate travelDate;
}
