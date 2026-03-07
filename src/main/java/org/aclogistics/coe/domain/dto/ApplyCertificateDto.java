package org.aclogistics.coe.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import java.time.LocalDate;
import java.util.Map;
import lombok.Data;
import org.aclogistics.coe.domain.enumeration.BusinessUnit;
import org.aclogistics.coe.domain.enumeration.Department;
import org.aclogistics.coe.domain.enumeration.EmploymentStatus;
import org.aclogistics.coe.domain.enumeration.Purpose;
import org.apache.commons.collections4.MapUtils;

/**
 * @author Rosendo Coquilla
 */
@Data
@JsonNaming(SnakeCaseStrategy.class)
public class ApplyCertificateDto implements Dto {

    @Serial
    private static final long serialVersionUID = 1093712730365435926L;

    @NotBlank(message = "Please provide your first name")
    private String firstName;

    @NotBlank(message = "Please provide your last name")
    private String lastName;

    @NotBlank(message = "Please provide your middle initial")
    private String middleInitial;

    @NotNull(message = "Please provide your business unit")
    private BusinessUnit businessUnit;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @NotNull(message = "Please provide your hired date")
    private LocalDate hiredDate;

    @NotBlank(message = "Please provide your position")
    private String position;

    @NotNull(message = "Please provide your department")
    private Department department;

    @NotNull(message = "Please provide your employment status")
    private EmploymentStatus employmentStatus;

    @NotNull(message = "Please indicate if you need to have your compensation included")
    private Boolean withCompensation;

    @NotNull(message = "Please provide your purpose")
    private Purpose purpose;

    // TODO: Refactor this to be a JsonSubType based on Purpose once the POC has been accepted
    private Map<String, String> additionalInfo;

    @NotBlank(message = "Please provide the addressee of the certificate")
    private String addressee;

    private String placeOfAddressee;

    private String requesterEmail = "email@email.com";// SecurityUtility.getUserEmail();

    private String requestedBy = "sen";// SecurityUtility.getUserFullName();

    @AssertTrue(message = "Please provide your destination, and line manager's name and email")
    public boolean isVisaApplicationPurposeWithAdditionalInfoValid() {
        if (!Purpose.VISA_APPLICATION.equals(this.purpose)) {
            return true;
        }

        return MapUtils.isNotEmpty(this.additionalInfo)
            && this.additionalInfo.containsKey("destination")
            && this.additionalInfo.containsKey("line_manager_first_name")
            && this.additionalInfo.containsKey("line_manager_last_name")
            && this.additionalInfo.containsKey("line_manager_email");
    }

    @AssertTrue(message = "Please provide your line manager's name and email")
    public boolean isApprovedLeaveApplicationPurposeWithAdditionalInfoValid() {
        if (!Purpose.APPROVED_LEAVE_APPLICATION.equals(this.purpose)) {
            return true;
        }

        return MapUtils.isNotEmpty(this.additionalInfo)
            && this.additionalInfo.containsKey("line_manager_first_name")
            && this.additionalInfo.containsKey("line_manager_last_name")
            && this.additionalInfo.containsKey("line_manager_email");
    }

    @AssertTrue(message = "Please indicate to whom you are requesting the certificate")
    public boolean isSchoolRequirementsPurposeWithAdditionalInfoValid() {
        if (!Purpose.SCHOOL_REQUIREMENTS.equals(this.purpose)) {
            return true;
        }

        return MapUtils.isNotEmpty(this.additionalInfo)
            && this.additionalInfo.containsKey("beneficiary");
    }
}
