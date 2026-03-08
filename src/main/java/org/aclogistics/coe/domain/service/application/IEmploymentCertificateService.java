package org.aclogistics.coe.domain.service;

import org.aclogistics.coe.domain.dto.application.ApplyCertificateDto;
import org.aclogistics.coe.domain.dto.transition.TransitionApplicationDto;

/**
 * @author Rosendo Coquilla
 */
public interface IEmploymentCertificateService {

    void apply(ApplyCertificateDto dto);
    void transitionToNextStatus(TransitionApplicationDto dto);
    void generateCertificate();
}
