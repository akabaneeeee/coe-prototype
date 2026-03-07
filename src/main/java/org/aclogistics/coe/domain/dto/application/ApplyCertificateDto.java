package org.aclogistics.coe.domain.dto.application;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import java.time.LocalDate;
import java.util.Objects;
import lombok.Data;
import org.aclogistics.coe.domain.dto.Dto;
import org.aclogistics.coe.domain.dto.application.additionalinfo.AdditionalInfo;
import org.aclogistics.coe.domain.dto.application.additionalinfo.ApprovedLeaveApplicationDetails;
import org.aclogistics.coe.domain.dto.application.additionalinfo.SchoolRequirementsDetails;
import org.aclogistics.coe.domain.dto.application.additionalinfo.TravelPurposesDetails;
import org.aclogistics.coe.domain.dto.application.additionalinfo.VisaApplicationDetails;
import org.aclogistics.coe.domain.enumeration.BusinessUnit;
import org.aclogistics.coe.domain.enumeration.Department;
import org.aclogistics.coe.domain.enumeration.EmploymentStatus;
import org.aclogistics.coe.domain.enumeration.Purpose;

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

    @Valid
    @JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = As.EXTERNAL_PROPERTY,
        property = "purpose",
        visible = true
    )
    @JsonSubTypes({
        @JsonSubTypes.Type(value = ApprovedLeaveApplicationDetails.class, name = "APPROVED_LEAVE_APPLICATION"),
        @JsonSubTypes.Type(value = VisaApplicationDetails.class, name = "VISA_APPLICATION"),
        @JsonSubTypes.Type(value = SchoolRequirementsDetails.class, name = "SCHOOL_REQUIREMENTS"),
        @JsonSubTypes.Type(value = TravelPurposesDetails.class, name = "TRAVEL_PURPOSES")
    })
    private AdditionalInfo additionalInfo;

    @NotBlank(message = "Please provide the addressee of the certificate")
    private String addressee;

    private String placeOfAddressee;

    private String requesterEmail = "email@email.com";// SecurityUtility.getUserEmail();

    private String requestedBy = "sen";// SecurityUtility.getUserFullName();

    @JsonIgnore
    @AssertTrue(message = "You must provide the needed additional information for the purpose that you have selected")
    public boolean isAdditionalInfoValid() {
        return switch (this.purpose) {
            case VISA_APPLICATION,
                 TRAVEL_PURPOSES,
                 APPROVED_LEAVE_APPLICATION,
                 SCHOOL_REQUIREMENTS -> Objects.nonNull(this.additionalInfo);
            default -> true;
        };
    }
}
