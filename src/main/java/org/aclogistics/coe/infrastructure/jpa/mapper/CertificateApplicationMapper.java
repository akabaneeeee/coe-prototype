package org.aclogistics.coe.infrastructure.jpa.mapper;

import org.aclogistics.coe.domain.model.CertificateApplication;
import org.aclogistics.coe.infrastructure.jpa.entity.CertificateApplicationEntity;
import org.apache.commons.collections4.CollectionUtils;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.MappingTarget;

/**
 * @author Rosendo Coquilla
 */
@Mapper(componentModel = ComponentModel.SPRING)
public interface CertificateApplicationMapper {

    CertificateApplication toModel(CertificateApplicationEntity entity);

    CertificateApplicationEntity toEntity(CertificateApplication model);

    @AfterMapping
    default void mapCertificateApplicationToMilestone(CertificateApplicationEntity entity, @MappingTarget CertificateApplication model) {
        if (CollectionUtils.isNotEmpty(model.getMilestones())) {
            entity.getMilestones().forEach(d -> d.setCertificateApplication(entity));
        }
    }

    @AfterMapping
    default void mapCertificateApplicationToGeneratedCertificate(CertificateApplicationEntity entity, @MappingTarget CertificateApplication model) {
        if (CollectionUtils.isNotEmpty(model.getGeneratedCertificates())) {
            entity.getGeneratedCertificates().forEach(d -> d.setCertificateApplication(entity));
        }
    }
}
