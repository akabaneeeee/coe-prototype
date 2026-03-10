package org.aclogistics.coe.domain.dto.filter;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
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
public class FilterDto implements Dto {

    @Serial
    private static final long serialVersionUID = 5903941154533019009L;

    @Builder.Default
    private List<BeneficiaryFilter> beneficiaries = new ArrayList<>();

    @Builder.Default
    private List<BusinessUnitFilter> businessUnits = new ArrayList<>();

    @Builder.Default
    private List<DepartmentFilter> departments = new ArrayList<>();

    @Builder.Default
    private List<PurposeFilter> purposes = new ArrayList<>();

    @Builder.Default
    private List<StatusFilter> statues = new ArrayList<>();
}
