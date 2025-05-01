package no.ntnu.stud.idatt2106.backend.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.jsonwebtoken.security.SignatureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * Test class for the GlobalExceptionHandler class.
 */
@ActiveProfiles("test")
@Import(GlobalExceptionHandler.class)
public class GlobalExceptionHandlerTest {

  private GlobalExceptionHandler handler;

  @BeforeEach
  void setUp() {
    handler = new GlobalExceptionHandler();
  }

  @Test
  void shouldHandleIllegalArgumentException() {
    IllegalArgumentException exception = new IllegalArgumentException("Invalid argument");
    ResponseEntity<String> response = handler.handleIllegalArgumentException(exception, null);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("Invalid token or request: Invalid argument", response.getBody());
  }

  @Test
  void shouldHandleMissingServletRequestPartException() {
    MissingServletRequestPartException exception = 
        new MissingServletRequestPartException("partName");
    ResponseEntity<String> response = handler.handleMissingServletRequestPartException(exception);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("Required part is missing: partName", response.getBody());
  }

  @Test
  void shouldHandleMethodArgumentTypeMismatchException() {
    MethodArgumentTypeMismatchException exception = 
        new MethodArgumentTypeMismatchException(null, null, null, null, null);
    ResponseEntity<String> response = handler.handleMethodArgumentTypeMismatchException(exception);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("Invalid parameter type: null", response.getBody());
  }
  
  @Test
  void shouldHandleMissingServletRequestParameterException() {
    MissingServletRequestParameterException exception = 
        new MissingServletRequestParameterException("paramName", "paramType");
    ResponseEntity<String> response = 
        handler.handleMissingServletRequestParameterException(exception);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("Required request parameter 'paramName' is missing.", response.getBody());
  }

  @Test
  void shouldHandleNoHandlerFoundException() {
    NoHandlerFoundException exception = new NoHandlerFoundException("GET", "/path", null);
    ResponseEntity<String> response = handler.handleNoHandlerFoundException(exception);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals("Endpoint not found: /path", response.getBody());
  }

  @Test
  void shouldHandleHttpRequestMethodNotSupportedException() {
    HttpRequestMethodNotSupportedException exception = 
        new HttpRequestMethodNotSupportedException("GET");
    ResponseEntity<String> response = 
        handler.handleHttpRequestMethodNotSupportedException(exception);
    assertEquals(HttpStatus.METHOD_NOT_ALLOWED, response.getStatusCode());
    assertEquals("Request method GET not supported for this endpoint.", response.getBody());
  }

  @Test
  void shouldHandleAuthenticationException() {
    AuthenticationException exception = new AuthenticationException("Authentication failed") {};
    ResponseEntity<String> response = handler.handleAuthenticationException(exception);
    assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    assertEquals("Authentication failed: Authentication failed", response.getBody());
  }

  @Test
  void shouldHandleSignatureException() {
    SignatureException exception = new SignatureException("Invalid signature");
    ResponseEntity<String> response = handler.handleSignatureException(exception, null);
    assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    assertEquals("Invalid token signature: Invalid signature", response.getBody());
  }

  @Test
  void shouldHandleException() {
    Exception exception = new Exception("General exception");
    ResponseEntity<String> response = handler.handleGlobalException(exception, null);
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    assertEquals("An unexpected error occurred: General exception", response.getBody());
  }
}
