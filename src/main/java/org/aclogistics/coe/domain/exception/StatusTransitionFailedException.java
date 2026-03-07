package org.aclogistics.coe.domain.exception;

/**
 * @author Rosendo Coquilla
 */
public class StatusTransitionFailedException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "Unable to transition the status %s using event %s";

    public StatusTransitionFailedException(String status, String event) {
        super(String.format(DEFAULT_MESSAGE, status, event));
    }
}
