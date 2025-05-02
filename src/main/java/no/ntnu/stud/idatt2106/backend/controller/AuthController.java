package no.ntnu.stud.idatt2106.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import no.ntnu.stud.idatt2106.backend.model.request.AdminInviteRequest;
import no.ntnu.stud.idatt2106.backend.model.request.AdminUpgradeRequest;
import no.ntnu.stud.idatt2106.backend.model.request.LoginRequest;
import no.ntnu.stud.idatt2106.backend.model.request.PasswordResetKeyRequest;
import no.ntnu.stud.idatt2106.backend.model.request.PasswordResetRequest;
import no.ntnu.stud.idatt2106.backend.model.request.RegisterRequest;
import no.ntnu.stud.idatt2106.backend.model.response.ChangeCredentialsResponse;
import no.ntnu.stud.idatt2106.backend.model.response.LoginResponse;
import no.ntnu.stud.idatt2106.backend.model.response.RegisterResponse;
import no.ntnu.stud.idatt2106.backend.model.update.CredentialsUpdate;
import no.ntnu.stud.idatt2106.backend.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

  /**
   * Validates a JWT token and returns a response indicating whether the token is valid or not.
   *
   * @param token the JWT token to validate
   * @return a ResponseEntity indicating whether the token is valid or not
   */
  @Operation(summary = "Validate JWT token", 
      description = "Verifies the validity of the provided JWT token")
  @PostMapping("/test")
  public ResponseEntity<Void> test(@RequestHeader("Authorization") String token) {
    service.validateToken(token);
    return ResponseEntity.ok().build();
  }

  /**
   * Requests a password reset link to be sent to the user's email address.
   * It generates a unique key and sends an email with the reset link.
   *
   * @param email the email address of the user requesting the password reset
   * @return a ResponseEntity indicating success or failure
   */
  @Operation(summary = "Request password reset", 
      description = "Sends a password reset link to the user's email address")
  @PostMapping("/request-password-reset")
  public ResponseEntity<Void> requestPasswordReset(@RequestBody PasswordResetKeyRequest email) {
    service.requestPasswordReset(email);
    logger.info("Password reset requested for email: {}", email);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  /**
   * Handles password reset requests. It verifies the provided key, and password
   * and updates the password.
   *
   * @param passwordChangeRequest the request containing the key and new password
   * @return a ResponseEntity indicating success or failure
   */
  @Operation(summary = "Reset password", 
      description = "Updates the user's password after verifying the provided key and password")
  @PostMapping("/reset-password")
  public ResponseEntity<Void> resetPassword(
      @RequestBody PasswordResetRequest passwordChangeRequest) {
    service.resetPassword(passwordChangeRequest);
    logger.info("Password reset successfully");
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  /**
   * Invites a user to become an admin by sending a registration key to their email address.
   * It generates a unique key and sends an email with the registration link.
   *
   * @param request the request containing the email address of the user to invite
   * @param token the JWT token of the user sending the invite
   * @return a ResponseEntity indicating success or failure
   */
  @Operation(summary = "Invite user to become admin", 
      description = "Sends a registration key to the user's email address")
  @PostMapping("/invite-admin")
  public ResponseEntity<Void> inviteAdmin(
      @RequestBody AdminInviteRequest request,
      @RequestHeader("Authorization") String token) {
    service.inviteAdmin(request, token);
    logger.info("Admin invite sent to email: {}", request.getUsername());
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  /**
   * Handles the request to accept an admin invitation.
   *
   * @param request the registration key provided in the invitation
   * @return a ResponseEntity indicating success or failure
   */
  @Operation(summary = "Accept admin invitation", 
      description = "Accepts the admin invitation using the provided registration key")
  @PostMapping("/accept-admin-invite")
  public ResponseEntity<Void> acceptAdminInvite(@RequestBody AdminUpgradeRequest request) {
    service.acceptAdminInvite(request);
    logger.info("Admin invite accepted with key: {}", request.getKey());
    return ResponseEntity.ok().build();
  }
}