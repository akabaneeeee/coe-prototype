package org.aclogistics.coe.infrastructure.jpa.application.exception;

/**
 * @author Rosendo Coquilla
 */
public class DuplicateReferenceNumberException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "The reference number already exists. Please try again.";

    public DuplicateReferenceNumberException() {
        super(DEFAULT_MESSAGE);
    }
}
