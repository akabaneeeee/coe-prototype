package org.aclogistics.coe.domain.model;

import java.io.Serial;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.aclogistics.coe.domain.enumeration.BusinessUnit;
import org.aclogistics.coe.domain.enumeration.Department;
import org.aclogistics.coe.domain.enumeration.EmploymentStatus;
import org.aclogistics.coe.domain.enumeration.Purpose;

/**
 * @author Rosendo Coquilla
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CertificateApplication implements Model {

    @Serial
    private static final long serialVersionUID = 6597162658900062621L;

    private Integer id;
    private String requesterEmail;
    private String referenceNumber;
    private String firstName;
    private String lastName;
    private String middleInitial;
    private BusinessUnit businessUnit;
    private LocalDate hiredDate;
    private String position;
    private Department department;
    private EmploymentStatus employmentStatus;

    @Builder.Default
    private boolean withCompensation = true;

    private BigDecimal annualCompensation;
    private Purpose purpose;
    private Map<String, String> additionalInfo;
    private String addressee;
    private String placeOfAddressee;
    private String requestedBy;
    private LocalDateTime requestedDt;
    private String modifiedBy;
    private LocalDateTime modifiedDt;

    @Builder.Default
    private Set<CertificateApplicationMilestone> milestones = new LinkedHashSet<>();

    @Builder.Default
    private Set<GeneratedCertificate> generatedCertificates = new LinkedHashSet<>();

    /**
     * Used to create the initial milestone upon creation of the application
     *
     * @param requestedBy - the user who requested the application
     * @param requestedDt - the date and time the application was requested
     */
    public void initializeMilestone(String requestedBy, LocalDateTime requestedDt) {
        this.milestones.add(new CertificateApplicationMilestone(requestedBy, requestedDt));
    }
}
