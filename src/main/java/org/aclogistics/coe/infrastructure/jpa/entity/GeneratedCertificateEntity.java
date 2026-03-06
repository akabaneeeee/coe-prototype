package org.aclogistics.coe.infrastructure.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.aclogistics.coe.infrastructure.jpa.JpaEntity;

/**
 * @author Rosendo Coquilla
 */
@Data
@Entity
@NoArgsConstructor
@Table(name = "generated_certificate")
@ToString(exclude = "certificateApplication")
@EqualsAndHashCode(exclude = "certificateApplication")
public class GeneratedCertificateEntity implements JpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "certificate_application_milestone_id_seq_gen")
    @SequenceGenerator(name = "certificate_application_milestone_id_seq_gen", sequenceName = "certificate_application_milestone_id_seq_seq", initialValue = 1000)
    private Integer id;

    @Column
    private Integer version;

    @Column(name = "s3_key")
    private String s3Key;

    @Column(name = "s3_bucket")
    private String s3Bucket;

    @Column
    private String transitionedBy;

    @Column
    private LocalDateTime transitionedDt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "certificate_application_id", nullable = false)
    private CertificateApplicationEntity certificateApplication;
}
