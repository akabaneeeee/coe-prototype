package org.aclogistics.coe.domain.dto.application.filtered;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.aclogistics.coe.domain.dto.Dto;

/**
 * @author Rosendo Coquilla
 */
@Data
@AllArgsConstructor
@JsonNaming(SnakeCaseStrategy.class)
public class PaginatedApplicationDto implements Dto {

    @Serial
    private static final long serialVersionUID = 7467721695074047919L;

    private Metadata metadata;
    private List<CertificateApplicationDto> applications = new ArrayList<>();

    public PaginatedApplicationDto(Metadata metadata) {
        this.metadata = metadata;
    }
}
