package org.aclogistics.coe.infrastructure.jpa.details.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Map;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.aclogistics.coe.domain.model.enumeration.BusinessUnit;
import org.aclogistics.coe.domain.model.enumeration.Department;
import org.aclogistics.coe.domain.model.enumeration.Purpose;
import org.aclogistics.coe.domain.model.enumeration.Status;
import org.aclogistics.coe.infrastructure.configuration.converter.TimezoneDateTimeConverter;
import org.aclogistics.coe.infrastructure.jpa.JpaEntity;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

/**
 * @author Rosendo Coquilla
 */
@Data
@Entity
@Immutable
@NoArgsConstructor
@Table(name = "v_certificate_application_latest_details")
public class LatestApplicationDetailsEntity implements JpaEntity {

    @Id
    private Integer id;

    @Column
    private String requesterEmail;

    @Column
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
    @Enumerated(EnumType.STRING)
    private Department department;

    @Column
    @Enumerated(EnumType.STRING)
    private Purpose purpose;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json")
    private Map<String, String> additionalInfo;

    @Column
    private String lineManagerFirstName;

    @Column
    private String lineManagerLastName;

    @Column
    private String lineManagerEmail;

    @Column
    private String requestedBy;

    @Column
    @Convert(converter = TimezoneDateTimeConverter.class)
    private LocalDateTime requestedDt;

    @Column
    @Enumerated(EnumType.STRING)
    private Status latestStatus;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json")
    private Map<Object, Object> statusDetails;

    @Column
    private String latestTransitionedBy;

    @Column
    @Convert(converter = TimezoneDateTimeConverter.class)
    private LocalDateTime latestTransitionedDt;

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
     * Used to construct the full name of the employee
     *
     * @return String - the full name
     */
    public String constructLineManagerFullName() {
        if (StringUtils.isBlank(this.lineManagerFirstName) && StringUtils.isBlank(this.lineManagerLastName)) {
            return "N/A";
        }

        return "%s %s".formatted(this.lineManagerFirstName, this.lineManagerLastName);
    }
}
