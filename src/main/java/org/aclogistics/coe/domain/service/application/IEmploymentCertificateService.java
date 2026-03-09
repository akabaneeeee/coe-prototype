package org.aclogistics.coe.domain.service.application;

import org.aclogistics.coe.domain.dto.application.ApplyCertificateDto;
import org.aclogistics.coe.domain.dto.application.CertificateApplicationDetails;
import org.aclogistics.coe.domain.dto.application.UpdateApplicationDetails;
import org.aclogistics.coe.domain.dto.application.filtered.GetFilteredApplicationDto;
import org.aclogistics.coe.domain.dto.application.filtered.PaginatedApplicationDto;
import org.aclogistics.coe.domain.dto.transition.TransitionApplicationDto;

/**
 * @author Rosendo Coquilla
 */
public interface IEmploymentCertificateService {

    void apply(ApplyCertificateDto dto);
    void transitionToNextStatus(TransitionApplicationDto dto);
    PaginatedApplicationDto getPaginatedApplications(GetFilteredApplicationDto dto);
    CertificateApplicationDetails getApplicationDetails(String referenceNumber);
    void updateApplication(UpdateApplicationDetails dto);
}
