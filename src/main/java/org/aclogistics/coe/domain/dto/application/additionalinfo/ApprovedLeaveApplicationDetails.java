package org.aclogistics.coe.domain.dto.application.additionalinfo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.io.Serial;
import lombok.Data;

/**
 * @author Rosendo Coquilla
 */
@Data
@JsonInclude(Include.NON_NULL)
@JsonNaming(SnakeCaseStrategy.class)
@JsonTypeName("APPROVED_LEAVE_APPLICATION")
public class ApprovedLeaveApplicationDetails implements AdditionalInfo, HasLineManagerEmail {

    @Serial
    private static final long serialVersionUID = 1250157949607728884L;

    @NotBlank(message = "Please provide your line manager's first name")
    private String lineManagerFirstName;

    @NotBlank(message = "Please provide your line manager's last name")
    private String lineManagerLastName;

    @Email(
        message = "The provided line manager's email is invalid",
        regexp = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
    )
    @NotBlank(message = "Please provide your line manager's email")
    private String lineManagerEmail;
}
