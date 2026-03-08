package org.aclogistics.coe.domain.exception;

/**
 * @author Rosendo Coquilla
 */
public class RecordNotFoundException extends RuntimeException {

    public RecordNotFoundException(String message) {
        super(message);
    }
}
