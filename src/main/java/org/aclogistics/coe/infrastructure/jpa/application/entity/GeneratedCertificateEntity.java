package org.aclogistics.coe.infrastructure.jpa.application.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
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
import org.aclogistics.coe.infrastructure.configuration.converter.TimezoneDateTimeConverter;
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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "generated_certificate_id_seq_gen")
    @SequenceGenerator(name = "generated_certificate_id_seq_gen", sequenceName = "generated_certificate_id_seq", initialValue = 1000)
    private Integer id;

    @Column
    private Short version;

    @Column
    private String fileName;

    @Column(name = "s3_key")
    private String s3Key;

    @Column(name = "s3_bucket")
    private String s3Bucket;

    @Column
    private String generatedBy;

    @Column
    @Convert(converter = TimezoneDateTimeConverter.class)
    private LocalDateTime generatedDt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "certificate_application_id", nullable = false)
    private CertificateApplicationEntity certificateApplication;
}
