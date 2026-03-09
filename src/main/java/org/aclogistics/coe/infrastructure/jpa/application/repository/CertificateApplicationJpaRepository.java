package org.aclogistics.coe.infrastructure.jpa.application.repository;

import org.aclogistics.coe.infrastructure.jpa.application.entity.CertificateApplicationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author Rosendo Coquilla
 */
public interface CertificateApplicationJpaRepository
    extends JpaRepository<CertificateApplicationEntity, Integer>,
    JpaSpecificationExecutor<CertificateApplicationEntity> {

    CertificateApplicationEntity findByReferenceNumber(String referenceNumber);
}
