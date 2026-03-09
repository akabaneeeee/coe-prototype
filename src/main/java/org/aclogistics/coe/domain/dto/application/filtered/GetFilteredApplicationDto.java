package org.aclogistics.coe.domain.dto.application.filtered;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.io.Serial;
import java.util.List;
import lombok.Data;
import org.aclogistics.coe.domain.dto.Dto;
import org.aclogistics.coe.domain.model.enumeration.BusinessUnit;
import org.aclogistics.coe.domain.model.enumeration.Department;
import org.aclogistics.coe.domain.model.enumeration.PageModule;
import org.aclogistics.coe.domain.model.enumeration.Purpose;
import org.aclogistics.coe.domain.model.enumeration.Status;

/**
 * @author Rosendo Coquilla
 */
@Data
@JsonNaming(SnakeCaseStrategy.class)
public class GetFilteredApplicationDto implements Dto {

    @Serial
    private static final long serialVersionUID = 7467721695074047919L;

    private int page = 1;
    private int limit = 10;
    private PageModule module;

    private String referenceNumber;
    private List<Status> statuses;
    private List<BusinessUnit> businessUnits;
    private List<Department> departments;
    private List<Purpose> purposes;

    private boolean isLineManager = false; // SecurityUtility.isLineManager();
    private String requesterEmail = "rosendo.coquilla+requester@aclogistics.com.ph"; // SecurityUtility.getUserEmail();

    public int buildPage() {
        if (this.page < 0 || this.page == 0) {
            return 0;
        }

        return this.page - 1;
    }

    public int buildLimit() {
        if (this.limit <= 0) {
            return 1;
        }

        return Math.min(this.limit, 100);
    }
}
