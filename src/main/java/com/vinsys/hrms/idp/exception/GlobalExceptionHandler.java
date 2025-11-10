package com.vinsys.hrms.idp.exception;

import com.vinsys.hrms.idp.dto.response.GenericResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Global exception handler for the IDP module.
 * <p>
 * This class centralizes exception management across all controller layers
 * within the application by extending {@link ResponseEntityExceptionHandler}.
 * It intercepts and formats exceptions into a consistent JSON structure
 * using {@link GenericResponse}.
 * </p>
 *
 * <h3>Features:</h3>
 * <ul>
 *     <li>Handles validation errors raised by {@code @Valid} annotated DTOs.</li>
 *     <li>Provides a fallback handler for unhandled exceptions.</li>
 *     <li>Ensures consistent API response format with status and message.</li>
 * </ul>
 *
 * @author
 * @since 1.0
 */
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Handles validation errors that occur when a request body annotated with {@code @Valid}
     * fails validation constraints.
     *
     * @param ex      the {@link MethodArgumentNotValidException} containing validation details
     * @param headers the HTTP headers for the response
     * @param status  the HTTP status code
     * @param request the current {@link WebRequest}
     * @return a {@link ResponseEntity} containing a {@link GenericResponse}
     *         with a descriptive validation error message
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        return ResponseEntity.badRequest()
                .body(GenericResponse.error("Validation failed"));
    }

    /**
     * Handles all uncaught exceptions globally.
     * <p>
     * Any exception not handled by a specific handler is caught here, ensuring
     * that the client receives a consistent error response rather than a stack trace.
     * </p>
     *
     * @param ex the exception that was thrown
     * @return a {@link ResponseEntity} with status 500 (Internal Server Error)
     *         containing a {@link GenericResponse} with the exception message
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<GenericResponse> handleAll(Exception ex) {
        ex.printStackTrace(); // Log for debugging; consider replacing with a logger
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(GenericResponse.error(ex.getMessage()));
    }
}