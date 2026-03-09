package org.aclogistics.coe.domain.dto.application.filtered;

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
@JsonNaming(SnakeCaseStrategy.class)
public class Metadata implements Dto {

    @Serial
    private static final long serialVersionUID = 3448586125340129840L;

    private int limit;
    private int currentPage;
    private int totalPages;
    private Boolean hasNext;
    private Boolean hasPrevious;
    private long total;
    private int currentCount;
    private long forApprovalCount;
}
