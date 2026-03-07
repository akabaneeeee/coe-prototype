package org.aclogistics.coe.infrastructure.jpa.api;

import jakarta.persistence.PersistenceException;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aclogistics.coe.domain.model.CertificateApplication;
import org.aclogistics.coe.domain.port.ICertificateApplicationRepository;
import org.aclogistics.coe.infrastructure.jpa.entity.CertificateApplicationEntity;
import org.aclogistics.coe.infrastructure.jpa.exception.CertificateApplicationPersistenceException;
import org.aclogistics.coe.infrastructure.jpa.exception.DuplicateReferenceNumberException;
import org.aclogistics.coe.infrastructure.jpa.mapper.CertificateApplicationMapper;
import org.aclogistics.coe.infrastructure.jpa.repository.CertificateApplicationJpaRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Rosendo Coquilla
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CertificateApplicationRepository implements ICertificateApplicationRepository {

    private static final String REFERENCE_NUMBER_UNIQUE_KEY = "reference_number_uq";

    private final CertificateApplicationJpaRepository repository;
    private final CertificateApplicationMapper mapper;

    @Override
    @Transactional
    @Retryable(retryFor = DuplicateReferenceNumberException.class, backoff = @Backoff(delay = 1000))
    public CertificateApplication save(CertificateApplication model) {
        if (Objects.isNull(model)) {
            throw new IllegalArgumentException("The provided model is null");
        }

        CertificateApplicationEntity entity = mapper.toEntity(model);

        if (Objects.isNull(entity)) {
            throw new IllegalArgumentException("The mapped entity is null");
        }

        try {
            return mapper.toModel(repository.save(entity));
        } catch (PersistenceException | DataAccessException ex) {
            if (StringUtils.isNotBlank(ex.getMessage()) && ex.getMessage().contains(REFERENCE_NUMBER_UNIQUE_KEY)) {
                log.error("The generated reference number is already in use. Will retry...");
                throw new DuplicateReferenceNumberException();
            }

            log.error("An error occurred while saving the certificate application", ex);
            throw new CertificateApplicationPersistenceException();
        }
    }
}
