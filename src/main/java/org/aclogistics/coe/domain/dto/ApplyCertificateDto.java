package org.aclogistics.coe.domain.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.io.Serial;
import java.time.LocalDate;
import java.util.Map;
import lombok.Data;
import org.aclogistics.coe.domain.enumeration.BusinessUnit;
import org.aclogistics.coe.domain.enumeration.Department;
import org.aclogistics.coe.domain.enumeration.EmploymentStatus;
import org.aclogistics.coe.domain.enumeration.Purpose;
import org.aclogistics.coe.domain.utility.SecurityUtility;

/**
 * @author Rosendo Coquilla
 */
@Data
@JsonNaming(SnakeCaseStrategy.class)
public class ApplyCertificateDto implements Dto {

    @Serial
    private static final long serialVersionUID = 1093712730365435926L;

    private String firstName;
    private String lastName;
    private String middleInitial;
    private BusinessUnit businessUnit;
    private LocalDate hiredDate;
    private String position;
    private Department department;
    private EmploymentStatus employmentStatus;
    private boolean withCompensation;
    private Purpose purpose;
    private Map<String, String> additionalInfo;
    private String addressee;
    private String placeOfAddressee;
    private String requesterEmail = SecurityUtility.getUserEmail();
    private String requestedBy = SecurityUtility.getUserFullName();
}
