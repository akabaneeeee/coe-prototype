package org.aclogistics.coe.domain.port;

import org.aclogistics.coe.domain.model.CertificateApplication;

/**
 * @author Rosendo Coquilla
 */
public interface ICertificateApplicationRepository {

    CertificateApplication save(CertificateApplication model);
}
