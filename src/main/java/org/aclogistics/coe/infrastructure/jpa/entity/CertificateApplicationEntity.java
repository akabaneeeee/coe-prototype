package org.aclogistics.coe.infrastructure.jpa.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.aclogistics.coe.domain.enumeration.BusinessUnit;
import org.aclogistics.coe.domain.enumeration.Department;
import org.aclogistics.coe.domain.enumeration.EmploymentStatus;
import org.aclogistics.coe.domain.enumeration.Purpose;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.type.SqlTypes;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * @author Rosendo Coquilla
 */
@Data
@Entity
@Audited
@NoArgsConstructor
@Table(name = "certificate_application")
@EntityListeners(AuditingEntityListener.class)
@ToString(exclude = { "milestones", "generatedCertificates" })
public class CertificateApplicationEntity implements JpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "certificate_application_id_seq_gen")
    @SequenceGenerator(name = "certificate_application_id_seq_gen", sequenceName = "certificate_application_id_seq", initialValue = 1000)
    private Integer id;

    @Column
    private String requesterEmail;

    @Column(unique = true)
    private String referenceNumber;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column
    private String middleInitial;

    @Column
    @Enumerated(EnumType.STRING)
    private BusinessUnit businessUnit;

    @Column
    private LocalDate hiredDate;

    @Column
    private String position;

    @Column
    @Enumerated(EnumType.STRING)
    private Department department;

    @Column
    @Enumerated(EnumType.STRING)
    private EmploymentStatus employmentStatus;

    @Column
    private boolean withCompensation;

    @Column
    private BigDecimal annualCompensation;

    @Column
    @Enumerated(EnumType.STRING)
    private Purpose purpose;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json")
    private Map<String, String> additionalInfo;

    @Column
    private String addressee;

    @Column
    private String placeOfAddressee;

    @Column
    private String requestedBy;

    @Column
    private LocalDateTime requestedDt;

    @Column
    private String modifiedBy;

    @Column
    private LocalDateTime modifiedDt;

    @NotAudited
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "certificateApplication", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CertificateApplicationMilestoneEntity> milestones = new LinkedHashSet<>();

    @NotAudited
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "certificateApplication", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<GeneratedCertificateEntity> generatedCertificates = new LinkedHashSet<>();
}
