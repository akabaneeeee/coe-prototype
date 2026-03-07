package org.aclogistics.coe.domain.service;

import org.aclogistics.coe.domain.dto.application.ApplyCertificateDto;

/**
 * @author Rosendo Coquilla
 */
public interface IEmploymentCertificateService {

    void apply(ApplyCertificateDto dto);
    void generateCertificate();
}
