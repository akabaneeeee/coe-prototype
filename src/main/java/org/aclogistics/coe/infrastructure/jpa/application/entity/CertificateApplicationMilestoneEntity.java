package org.aclogistics.coe.infrastructure.jpa.application.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Map;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.aclogistics.coe.domain.model.enumeration.Status;
import org.aclogistics.coe.infrastructure.configuration.converter.TimezoneDateTimeConverter;
import org.aclogistics.coe.infrastructure.jpa.JpaEntity;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

/**
 * @author Rosendo Coquilla
 */
@Data
@Entity
@NoArgsConstructor
@ToString(exclude = "certificateApplication")
@Table(name = "certificate_application_milestone")
@EqualsAndHashCode(exclude = "certificateApplication")
public class CertificateApplicationMilestoneEntity implements JpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "certificate_application_milestone_id_seq_gen")
    @SequenceGenerator(name = "certificate_application_milestone_id_seq_gen", sequenceName = "certificate_application_milestone_id_seq", initialValue = 1000)
    private Integer id;

    @Column
    @Enumerated(EnumType.STRING)
    private Status status;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json")
    private Map<Object, Object> statusDetails;

    @Column
    private String transitionedBy;

    @Column
    @Convert(converter = TimezoneDateTimeConverter.class)
    private LocalDateTime transitionedDt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "certificate_application_id", nullable = false)
    private CertificateApplicationEntity certificateApplication;
}
