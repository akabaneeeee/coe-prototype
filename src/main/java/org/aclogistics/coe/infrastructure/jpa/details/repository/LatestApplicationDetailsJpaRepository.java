package org.aclogistics.coe.infrastructure.jpa.details.repository;

import org.aclogistics.coe.infrastructure.jpa.details.entity.LatestApplicationDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author Rosendo Coquilla
 */
public interface LatestApplicationDetailsJpaRepository
    extends JpaRepository<LatestApplicationDetailsEntity, Integer>,
    JpaSpecificationExecutor<LatestApplicationDetailsEntity> {

}
