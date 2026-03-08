package org.aclogistics.coe.application.handler;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.aclogistics.coe.domain.exception.RecordNotFoundException;
import org.aclogistics.coe.domain.exception.StatusTransitionFailedException;
import org.aclogistics.coe.domain.exception.StatusVerificationFailedException;
import org.aclogistics.coe.infrastructure.jpa.exception.CertificateApplicationPersistenceException;
import org.aclogistics.coe.infrastructure.jpa.exception.DuplicateReferenceNumberException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.Nullable;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * @author Rosendo Coquilla
 */
@Slf4j
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RestControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
        Exception ex, @Nullable Object body, HttpHeaders headers,
        HttpStatusCode statusCode, WebRequest request
    ) {
        var errorMessage = "Something went wrong...";

        logException(ex);

        return createResponse(
            new ApiErrorDto(HttpStatus.INTERNAL_SERVER_ERROR, errorMessage, "Unexpected error occurred. Please contact your system administrator for further assistance.")
        );
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
        MethodArgumentNotValidException ex, HttpHeaders headers,
        HttpStatusCode status, WebRequest request
    ) {
        var errorMessage = "Constraint Violation Error";

        logException(ex);

        Map<String, List<String>> errors = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .collect(Collectors.groupingBy(
                FieldError::getField,
                Collectors.mapping(FieldError::getDefaultMessage, Collectors.toList())
            ));

        return createResponse(
            new ApiErrorDto(HttpStatus.BAD_REQUEST, errorMessage, errors)
        );
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
        HttpRequestMethodNotSupportedException ex, HttpHeaders headers,
        HttpStatusCode status, WebRequest request
    ) {
        logException(ex);

        return createResponse(
            new ApiErrorDto(HttpStatus.METHOD_NOT_ALLOWED, "Method not allowed", ex.getMessage())
        );
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
        HttpMessageNotReadableException ex, HttpHeaders headers,
        HttpStatusCode status, WebRequest request
    ) {
        var errorMessage = "Malformed JSON body";

        logException(ex);

        return createResponse(
            new ApiErrorDto(HttpStatus.BAD_REQUEST, errorMessage, "Unable to parse the provided request")
        );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<Object> handleMethodArgumentTypeMismatch(
        MethodArgumentTypeMismatchException ex, WebRequest request) {
        var errorMessage = String
            .format("Parameter '%s' of value '%s' could not be converted to type '%s'",
                ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName());

        logException(ex);

        return createResponse(
            new ApiErrorDto(HttpStatus.BAD_REQUEST, errorMessage, ex.getMessage())
        );
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleRuntimeException(
        RuntimeException ex, WebRequest request
    ) {
        var errorMessage = "Something went wrong...";

        logException(ex);

        return createResponse(
            new ApiErrorDto(HttpStatus.INTERNAL_SERVER_ERROR, errorMessage, ex.getMessage())
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(
        IllegalArgumentException ex, WebRequest request
    ) {
        var errorMessage = "Malformed Request";

        logException(ex);

        return createResponse(
            new ApiErrorDto(HttpStatus.BAD_REQUEST, errorMessage, ex.getMessage())
        );
    }

    @ExceptionHandler(DuplicateReferenceNumberException.class)
    public ResponseEntity<Object> handleDuplicateReferenceNumberException(
        DuplicateReferenceNumberException ex, WebRequest request
    ) {
        var errorMessage = "Duplicate reference number";

        logException(ex);

        return createResponse(
            new ApiErrorDto(HttpStatus.UNPROCESSABLE_ENTITY, errorMessage, ex.getMessage())
        );
    }

    @ExceptionHandler(CertificateApplicationPersistenceException.class)
    public ResponseEntity<Object> handleCertificateApplicationPersistenceException(
        CertificateApplicationPersistenceException ex, WebRequest request
    ) {
        var errorMessage = "Internal Server Error";

        logException(ex);

        return createResponse(
            new ApiErrorDto(HttpStatus.INTERNAL_SERVER_ERROR, errorMessage, ex.getMessage())
        );
    }

    @ExceptionHandler(StatusTransitionFailedException.class)
    public ResponseEntity<Object> handleStatusTransitionFailedException(
        StatusTransitionFailedException ex, WebRequest request
    ) {
        var errorMessage = "Status transition failed";

        logException(ex);

        return createResponse(
            new ApiErrorDto(HttpStatus.UNPROCESSABLE_ENTITY, errorMessage, ex.getMessage())
        );
    }

    @ExceptionHandler(StatusVerificationFailedException.class)
    public ResponseEntity<Object> handleStatusVerificationFailedException(
        StatusVerificationFailedException ex, WebRequest request
    ) {
        var errorMessage = "Status verification failed";

        logException(ex);

        return createResponse(
            new ApiErrorDto(HttpStatus.UNPROCESSABLE_ENTITY, errorMessage, ex.getMessage())
        );
    }

    @ExceptionHandler(RecordNotFoundException.class)
    public ResponseEntity<Object> handleRecordNotFoundException(
        RecordNotFoundException ex, WebRequest request
    ) {
        var errorMessage = "Record not found";

        logException(ex);

        return createResponse(
            new ApiErrorDto(HttpStatus.NOT_FOUND, errorMessage, ex.getMessage())
        );
    }

    private ResponseEntity<Object> createResponse(ApiErrorDto errorDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PROBLEM_JSON);

        return ResponseEntity.status(errorDto.getStatus())
            .headers(headers)
            .body(errorDto);
    }

    private void logException(Exception ex) {
        log.error("Exception: ", ex);
    }
}
