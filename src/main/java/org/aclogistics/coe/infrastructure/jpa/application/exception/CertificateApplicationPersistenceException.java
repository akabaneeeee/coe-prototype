package org.aclogistics.coe.infrastructure.jpa.application.exception;

/**
 * @author Rosendo Coquilla
 */
public class CertificateApplicationPersistenceException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "Something went wrong while saving the application. Please contact your administrator.";

    public CertificateApplicationPersistenceException() {
        super(DEFAULT_MESSAGE);
    }
}
