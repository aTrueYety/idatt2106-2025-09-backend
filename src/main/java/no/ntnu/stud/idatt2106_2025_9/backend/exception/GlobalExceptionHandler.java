package no.ntnu.stud.idatt2106_2025_9.backend.exception;

import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * Global exception handler for the application.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    Logger logger = org.slf4j.LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handles IllegalArgumentException.
     *
     * @param ex the IllegalArgumentException
     * @return the ResponseEntity with a 400 status and error message
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(
            IllegalArgumentException ex, WebRequest request) {
        logger.error("Illegal argument: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(
                "Invalid token or request: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles MissingServletRequestPartException.
     *
     * @param ex the MissingServletRequestPartException
     * @return the ResponseEntity with a 400 status and error message
     */
    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<String> handleMissingServletRequestPartException(
            MissingServletRequestPartException ex) {
        logger.error("Missing request part: {}", ex.getRequestPartName(), ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Required part is missing: " + ex.getRequestPartName());
    }

    /**
     * Handles MethodArgumentTypeMismatchException.
     *
     * @param ex the MethodArgumentTypeMismatchException
     * @return the ResponseEntity with a 400 status and error message
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException ex) {
        logger.error("Method argument type mismatch: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Invalid parameter type: " + ex.getName());
    }

    /**
     * Handles MissingServletRequestParameterException.
     *
     * @param ex the MissingServletRequestParameterException
     * @return the ResponseEntity with a 400 status and error message
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<String> handleMissingServletRequestParameterException(
            MissingServletRequestParameterException ex) {
        logger.error("Missing request parameter: {}", ex.getParameterName(), ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Required request parameter '" + ex.getParameterName() + "' is missing.");
    }

    /**
     * Handles NoHandlerFoundException.
     *
     * @param ex the NoHandlerFoundException
     * @return the ResponseEntity with a 404 status and error message
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<String> handleNoHandlerFoundException(
            NoHandlerFoundException ex) {
        logger.error("No handler found for request: {}", ex.getRequestURL(), ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Endpoint not found: " + ex.getRequestURL());
    }

    /**
     * Handles HttpRequestMethodNotSupportedException.
     *
     * @param ex the HttpRequestMethodNotSupportedException
     * @return the ResponseEntity with a 405 status and error message
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<String> handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException ex) {
        logger.error("Request method not supported: {}", ex.getMethod(), ex);
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body("Request method " + ex.getMethod() + " not supported for this endpoint.");
    }

    /**
     * Handles AuthenticationException.
     *
     * @param ex the AuthenticationException
     * @return the ResponseEntity with a 401 status and error message
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<String> handleAuthenticationException(AuthenticationException ex) {
        logger.error("Authentication failed: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Authentication failed: " + ex.getMessage());
    }

    /**
     * Handles all other exceptions.
     *
     * @param ex the Exception
     * @param request the WebRequest
     * @return the ResponseEntity with a 500 status and error message
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGlobalException(Exception ex, WebRequest request) {
        logger.error("An unexpected error occurred: {}", ex.getMessage());
        return new ResponseEntity<>(
                "An unexpected error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}