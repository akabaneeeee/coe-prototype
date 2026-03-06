package org.aclogistics.coe.infrastructure.jpa.repository;

import org.aclogistics.coe.infrastructure.jpa.entity.CertificateApplicationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author Rosendo Coquilla
 */
public interface CertificateApplicationRepository
    extends JpaRepository<CertificateApplicationEntity, Integer>,
    JpaSpecificationExecutor<CertificateApplicationEntity> {

}
