package org.aclogistics.coe.domain.model;

import java.io.Serial;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
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
@NoArgsConstructor
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
    private boolean withCompensation;
    private BigDecimal annualCompensation;
    private Purpose purpose;
    private Map<String, String> additionalInfo;
    private String addressee;
    private String placeOfAddressee;
    private String requestedBy;
    private LocalDateTime requestedDt;
    private String modifiedBy;
    private LocalDateTime modifiedDt;
    private Set<CertificateApplicationMilestone> milestones = new LinkedHashSet<>();
    private Set<GeneratedCertificate> generatedCertificates = new LinkedHashSet<>();
}
