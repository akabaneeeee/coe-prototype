package org.aclogistics.coe.application.handler;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.aclogistics.coe.domain.utility.DateTimeHelper;
import org.springframework.http.HttpStatus;

/**
 * @author Rosendo Coquilla
 */
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name = "RestErrorDetails", description = "Error Details Response")
public class ApiErrorDto implements Serializable {

    @Serial
    private static final long serialVersionUID = -4327552202345032677L;

    @Schema(example = "2026-01-01 23:59:59")
    private String timestamp;

    @Schema(example = "NOT_FOUND")
    private HttpStatus status;

    @Schema(example = "Validation error")
    private String message;

    @Schema(example = "The resource does not exist")
    private Object details;

    public ApiErrorDto(HttpStatus status, String message, Object details) {
        this.timestamp = DateTimeHelper.getTodayInPHTDefaultStringFormat();
        this.message = message;
        this.status = status;
        this.details = details;
    }
}
