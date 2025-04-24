package no.ntnu.stud.idatt2106.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import no.ntnu.stud.idatt2106.backend.model.request.LoginRequest;
import no.ntnu.stud.idatt2106.backend.model.request.RegisterRequest;
import no.ntnu.stud.idatt2106.backend.model.response.ChangeCredentialsResponse;
import no.ntnu.stud.idatt2106.backend.model.response.LoginResponse;
import no.ntnu.stud.idatt2106.backend.model.response.RegisterResponse;
import no.ntnu.stud.idatt2106.backend.model.update.CredentialsUpdate;
import no.ntnu.stud.idatt2106.backend.service.AuthService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * This is a controller class responsible for authentication and security-related operations,
 * like login, registration, token management and changing a user's password.
 * It provides endpoints for user registration, login and changing of password.
 */
@Tag(name = "Authentication", 
    description = "Endpoints for user authentication and security operations")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

  @Autowired
  private AuthService service;

  Logger logger = LoggerFactory.getLogger(AuthController.class);

  /**
   * This method handles user registration requests. It creates a new user in the
   * system and returns a response indicating success or failure.
   *
   * @param registerRequest the registration request containing user details
   * @return a ResponseEntity containing the registration response or an error message
   */
  @Operation(summary = "Register a new user", description = "Creates a new user in the system")
  @PostMapping("/register")
  public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
    RegisterResponse registerResponse = service.register(registerRequest);
    logger.info("User registered successfully: {}", registerRequest.getUsername());
    return ResponseEntity.ok(registerResponse);
  }

  /**
   * This method handles user login requests. It verifies the user's credentials
   * and returns a JWT token if successful.
   *
   * @param loginRequest the login request containing the user's credentials
   * @return a ResponseEntity containing the login response or an error message
   */
  @Operation(summary = "Login a user", 
      description = "Verifies user credentials and returns a JWT token")
  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
    LoginResponse loginResponse = service.login(loginRequest);
    logger.info("User logged in successfully: {}", loginRequest.getUsername());
    return ResponseEntity.ok(loginResponse);
  }

  /**
   * This method handles requests to change a user's password.
   * It verifies the user's identity and updates the password if valid.
   *
   * @param credentialsUpdate the request containing the current and new passwords
   * @return a ResponseEntity indicating success or failure
   */
  @Operation(summary = "Change user credentials", 
      description = "Updates the user's password after verifying identity")
  @PostMapping("/change-credentials")
  public ResponseEntity<?> changeCredentials(
      @RequestBody CredentialsUpdate credentialsUpdate,
      @RequestHeader("Authorization") String token
  ) {
    ChangeCredentialsResponse changeCredentialsResponse = service
        .changeCredentials(credentialsUpdate, token);
    logger.info("User credentials changed successfully");
    return ResponseEntity.ok(changeCredentialsResponse);
  }
}