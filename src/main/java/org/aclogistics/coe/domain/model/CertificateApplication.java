package org.aclogistics.coe.domain.model;

import java.io.Serial;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.aclogistics.coe.domain.model.enumeration.BusinessUnit;
import org.aclogistics.coe.domain.model.enumeration.Department;
import org.aclogistics.coe.domain.model.enumeration.EmploymentStatus;
import org.aclogistics.coe.domain.model.enumeration.Purpose;
import org.aclogistics.coe.domain.exception.RecordNotFoundException;

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
    private String lineManagerEmail;
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

    /**
     * Used to get the latest milestone of the application
     *
     * @return CertificateApplicationMilestone - the latest milestone
     */
    public CertificateApplicationMilestone getLatestMilestone() {
        return this.milestones.stream()
            .max(Comparator.comparing(CertificateApplicationMilestone::getTransitionedDt))
            .orElseThrow(() -> new RecordNotFoundException("The application has no existing milestones"));
    }

    /**
     * Used to add a new milestone
     *
     * @param milestone - the milestone to add
     */
    public void addMilestone(CertificateApplicationMilestone milestone) {
        this.milestones.add(milestone);
    }

    /**
     * Used to construct the full name of the employee
     *
     * @return String - the full name
     */
    public String constructFullName() {
        var cleanedMiddleInitial = this.middleInitial;
        if (!this.middleInitial.contains(".")) {
            cleanedMiddleInitial = this.middleInitial + ".";
        }

        return "%s %s %s".formatted(this.firstName, cleanedMiddleInitial, this.lastName);
    }

    /**
     * Used to get the next certificate version; when the application is first created, the version will be 1
     *
     * @return Short - the next certificate version
     */
    public Short getNextCertificateVersion() {
        Short latestVersion = this.generatedCertificates.stream()
            .max(Comparator.comparing(GeneratedCertificate::getVersion))
            .map(GeneratedCertificate::getVersion)
            .orElse((short) 0);

        return (short) (latestVersion + 1);
    }

    /**
     * Used to add a new generated certificate
     *
     * @param generatedCertificate - the generated certificate
     */
    public void addGeneratedCertificate(GeneratedCertificate generatedCertificate) {
        this.generatedCertificates.add(generatedCertificate);
    }

    public GeneratedCertificate getLatestGeneratedCertificate() {
        return this.generatedCertificates.stream()
            .max(Comparator.comparing(GeneratedCertificate::getGeneratedDt))
            .orElse(null);
    }
}
