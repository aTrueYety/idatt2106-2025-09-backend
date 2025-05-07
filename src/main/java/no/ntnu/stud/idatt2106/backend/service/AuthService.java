package no.ntnu.stud.idatt2106.backend.service;

import jakarta.mail.MessagingException;
import no.ntnu.stud.idatt2106.backend.model.base.AdminRegistrationKey;
import no.ntnu.stud.idatt2106.backend.model.base.PasswordResetKey;
import no.ntnu.stud.idatt2106.backend.model.base.User;
import no.ntnu.stud.idatt2106.backend.model.request.AdminInviteRequest;
import no.ntnu.stud.idatt2106.backend.model.request.AdminRemoveRequest;
import no.ntnu.stud.idatt2106.backend.model.request.AdminUpgradeRequest;
import no.ntnu.stud.idatt2106.backend.model.request.LoginRequest;
import no.ntnu.stud.idatt2106.backend.model.request.PasswordResetKeyRequest;
import no.ntnu.stud.idatt2106.backend.model.request.PasswordResetRequest;
import no.ntnu.stud.idatt2106.backend.model.request.RegisterRequest;
import no.ntnu.stud.idatt2106.backend.model.response.ChangeCredentialsResponse;
import no.ntnu.stud.idatt2106.backend.model.response.LoginResponse;
import no.ntnu.stud.idatt2106.backend.model.response.RegisterResponse;
import no.ntnu.stud.idatt2106.backend.model.update.CredentialsUpdate;
import no.ntnu.stud.idatt2106.backend.util.EmailTemplates;
import no.ntnu.stud.idatt2106.backend.util.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service class for handling auth-related operations such as registration and
 * login.
 */
@Service
public class AuthService {

  @Autowired
  private JwtService jwtService;
  @Autowired
  AuthenticationManager authManager;
  @Autowired
  private UserService userService;
  @Autowired
  private EmailService emailService;
  @Autowired
  private PasswordResetService passwordResetKeyService;
  @Autowired
  private AdminRegistrationKeyService adminRegistrationKeyService;

  private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

  /**
   * Registers a new user in the system.
   */
  public RegisterResponse register(RegisterRequest registerRequest) {

    Validate.that(registerRequest.getUsername(),
        Validate.isNotBlankOrNull(), "Username cannot be blank or null");
    Validate.that(userService.getUserByUsername(registerRequest.getUsername()),
        Validate.isNull(), "Username is not available");

    Validate.that(registerRequest.getPassword(),
        Validate.isNotBlankOrNull(),
        "New password cannot be blank or null");
    Validate.that(isPasswordValid(registerRequest.getPassword()),
        Validate.isTrue(),
        "New password must be at least 8 characters long, including both a letter and a digit");

    Validate.that(registerRequest.getEmail().matches(
        "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"),
        Validate.isTrue(), "Email is not valid");
    Validate.that(userService.getUserByEmail(registerRequest.getEmail()),
        Validate.isNull(), "Email is already in use");

    User user = new User();

    user.setUsername(registerRequest.getUsername());
    user.setPassword(encoder.encode(registerRequest.getPassword()));
    user.setEmail(registerRequest.getEmail());
    userService.addUser(user);

    String token = authenticateUser(registerRequest.getUsername(), registerRequest.getPassword());

    String htmlContent = EmailTemplates.getWelcomeEmailTemplate(user.getUsername());
    try {
      emailService.sendHtmlEmail(user.getEmail(), "Welcome to Our Platform", htmlContent);
    } catch (MessagingException e) {
      throw new RuntimeException("Failed to send welcome email", e);
    }

    return new RegisterResponse("Registration successful!", token);
  }

  /**
   * Verifies the login credentials of a user.
   */
  public LoginResponse login(LoginRequest loginRequest) {
    if (loginRequest.getUsername() == null) {
      throw new IllegalArgumentException("Username cannot be null or empty");      
    }
    if (loginRequest.getPassword() == null) {
      throw new IllegalArgumentException("Password cannot be null or empty");
      
    }
    
    String token = authenticateUser(loginRequest.getUsername(), loginRequest.getPassword());

    return new LoginResponse("Login successful!", token);
  }

  /**
   * Authenticates the user and generates a token.
   */
  private String authenticateUser(String username, String password) {
    Authentication authentication = authManager
        .authenticate(new UsernamePasswordAuthenticationToken(username, password));

    if (authentication.isAuthenticated() && userService.getUserByUsername(username) != null) {
      User user = userService.getUserByUsername(username);
      return jwtService.generateToken(
          user.getUsername(), user.getId(), user.isAdmin(), user.isSuperAdmin());
    } else {
      return null;
    }
  }

  /**
   * changes the credentials of a user.
   *
   * @param credentialsUpdate request containing old and new password
   *                          along with desired username
   * @return response containing feedback message and new token
   */
  public ChangeCredentialsResponse changeCredentials(
      CredentialsUpdate credentialsUpdate,
      String token) {
    User user = userService.getUserByUsername(
        jwtService.extractUserName(token.substring(7)));
    Validate.that(credentialsUpdate.getUsername(),
        Validate.isNotBlankOrNull(),
        "New username cannot be blank or null");
    // check if new username is availanle if the request includes a new username,
    // skip if user is keeping old username
    if (!user.getUsername().equals(credentialsUpdate.getUsername())) {
      Validate.that(userService.getUserByUsername(credentialsUpdate.getUsername()),
          Validate.isNull(),
          "New username is not available");
    }
    Validate.that(encoder.matches(credentialsUpdate.getCurrentPassword(),
        user.getPassword()),
        Validate.isTrue(),
        "Current password is incorrect");
    Validate.that(credentialsUpdate.getNewPassword(),
        Validate.isNotBlankOrNull(),
        "New password cannot be blank or null");
    Validate.that(isPasswordValid(credentialsUpdate.getNewPassword()),
        Validate.isTrue(),
        "New password must be at least 8 characters long, including both a letter and a digit");
    Validate.that(
        credentialsUpdate.getCurrentPassword()
            .equals(credentialsUpdate.getNewPassword()),
        Validate.isFalse(),
        "New password cannot be the same as current password");

    // saving the new credentials
    user.setPassword(encoder.encode(credentialsUpdate.getNewPassword()));
    user.setUsername(credentialsUpdate.getUsername());
    userService.updateUserCredentials(user);

    String newToken = authenticateUser(
        credentialsUpdate.getUsername(),
        credentialsUpdate.getNewPassword());
    return new ChangeCredentialsResponse("Password changed successfully", newToken);
  }

  /**
   * Verifies that a password is valid.
   *
   * @param password password to check validity of
   * @return true if the password is valid,
   *         meaning it is atleast 8 characters long and contains both a letter
   *         and digit,
   *         false if otherwise
   */
  private boolean isPasswordValid(String password) {
    if (password == null || password.length() < 8) {
      return false;
    }
    boolean hasLetter = false;
    boolean hasNumber = false;
    for (char c : password.toCharArray()) {
      if (Character.isLetter(c)) {
        hasLetter = true;
      } else if (Character.isDigit(c)) {
        hasNumber = true;
      }
      if (hasLetter && hasNumber) {
        return true;
      }
    }
    return false;
  }

  /**
   * Validates the provided JWT token.
   *
   * @param token the JWT token to validate
   */
  public void validateToken(String token) {
    if (token == null) {
      throw new IllegalArgumentException("Token cannot be null or empty");
    }
    jwtService.extractUserName(token.substring(7));
  }

  /**
   * Requests a password reset link to be sent to the user's email address.
   * It generates a unique key and sends an email with the reset link.
   *
   * @param request the email address of the user requesting the password reset
   */
  public void requestPasswordReset(PasswordResetKeyRequest request) {
    Validate.that(request.getEmail(), Validate.isNotBlankOrNull(), "Email cannot be blank or null");
    Validate.that(request.getEmail().matches(
        "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"),
        Validate.isTrue(), "Email is not valid");

    User user = userService.getUserByEmail(request.getEmail());
    Validate.that(user, Validate.isNotNull(), "User not found");

    String key = passwordResetKeyService.createPasswordResetKey(user.getId());

    String htmlContent = EmailTemplates.getPasswordResetTemplate(key);
    try {
      emailService.sendHtmlEmail(user.getEmail(), "Tilbakestill passord", htmlContent);
    } catch (MessagingException e) {
      throw new RuntimeException("Failed to send password reset email", e);
    }
  }

  /**
   * Handles password reset requests. It verifies the provided key, and password
   * and updates the password.
   *
   * @param request the request containing the key and new password
   */
  public void resetPassword(PasswordResetRequest request) {
    Validate.isValid(isPasswordValid(request.getNewPassword()), 
        "New password must be at least 8 characters long, including both a letter and a digit");
    Validate.that(request.getKey(), Validate.isNotBlankOrNull(), "Key cannot be blank or null");

    PasswordResetKey passwordResetKey = passwordResetKeyService.findByKey(request.getKey());
    Validate.that(passwordResetKey, Validate.isNotNull(), "Key not found");
    User user = userService.getUserById(passwordResetKey.getUserId());
    Validate.that(user, Validate.isNotNull(), "User not found");
    user.setPassword(encoder.encode(request.getNewPassword()));
    userService.updateUserCredentials(user);
    passwordResetKeyService.deletePasswordResetKey(request.getKey());
  }

  /**
   * Invites a user to become an admin by sending an email with a registration key.
   *
   * @param request the request containing the user to invite
   * @param token   the JWT token of the user sending the invite
   */
  public void inviteAdmin(AdminInviteRequest request, String token) {
    Validate.that(request.getUsername(),
        Validate.isNotBlankOrNull(), "Username cannot be blank or null");
    Validate.that(token, Validate.isNotBlankOrNull(), "Token cannot be blank or null");

    User sender = userService.getUserByUsername(
        jwtService.extractUserName(token.substring(7)));
    Validate.that(sender, Validate.isNotNull(), "Sender not found");
    Validate.that(sender.isSuperAdmin(), Validate.isTrue(), "Sender is not a super admin");

    User user = userService.getUserByUsername(request.getUsername());
    Validate.that(user, Validate.isNotNull(), "User not found");
    Validate.that(user.isAdmin(), Validate.isFalse(), "User is already an admin");
    Validate.that(adminRegistrationKeyService.findByUserId(user.getId()),
        Validate.isNull(), "User already has a registration key");

    String registrationKey = adminRegistrationKeyService
        .createAdminRegistrationKey(user.getId());

    String htmlContent = EmailTemplates.getAdminUpgradeTemplate(
        request.getUsername(), registrationKey);
    try {
      emailService.sendHtmlEmail(user.getEmail(), "Registrer deg som admin", htmlContent);
    } catch (MessagingException e) {
      throw new RuntimeException("Failed to send admin registration email", e);
    }
  }

  /**
   * Accepts an admin registration key and registers the user as an admin.
   *
   * @param request the registration key provided in the invitation
   * @param token   the JWT token of the user accepting the invite
   */
  public LoginResponse acceptAdminInvite(AdminUpgradeRequest request, String token) {
    Validate.that(request.getKey(), Validate.isNotBlankOrNull(), "Key cannot be blank or null");

    AdminRegistrationKey registrationKey = adminRegistrationKeyService.findByKey(request.getKey());
    Validate.that(registrationKey, Validate.isNotNull(), "Admin invitation not found");
    User userInKey = userService.getUserById(registrationKey.getUserId());
    Validate.that(userInKey, Validate.isNotNull(), "User not found");

    User sender = userService.getUserByUsername(
        jwtService.extractUserName(token.substring(7)));
    Validate.that(sender, Validate.isNotNull(), "Sender not found");
    Validate.that(userInKey.getId().equals(sender.getId()),
        Validate.isTrue(), "You can only accept your own invitations");
    
    userInKey.setAdmin(true);
    userService.updateUserCredentials(userInKey);
    adminRegistrationKeyService.deleteByKey(request.getKey());

    String newToken = jwtService.generateToken(
        userInKey.getUsername(), userInKey.getId(), userInKey.isAdmin(), userInKey.isSuperAdmin());
    return new LoginResponse("Admin registration successful!", newToken);
  }

  /**
   * Deletes an admin registration key.
   *
   * @param request the request containing the registration key to delete
   */
  public void deleteAdminRegistrationKey(AdminRemoveRequest request) {
    Validate.that(
          request.getUsername(), Validate.isNotBlankOrNull(), "Username cannot be blank or null");

    User user = userService.getUserByUsername(request.getUsername());

    AdminRegistrationKey registrationKey = 
        adminRegistrationKeyService.findByUserId(user.getId());
    Validate.that(registrationKey, Validate.isNotNull(), "Admin invitation not found");

    adminRegistrationKeyService.deleteByUserId(user.getId());
  }

  /**
   * Removes admin status from a user.
   *
   * @param request the request containing the user ID to remove admin status from
   * @param token  the JWT token of the user sending the request
   */
  public void removeAdmin(AdminRemoveRequest request, String token) {
    Validate.that(token, Validate.isNotBlankOrNull(), "Token cannot be blank or null");

    User sender = userService.getUserByUsername(
        jwtService.extractUserName(token.substring(7)));
    Validate.that(sender, Validate.isNotNull(), "Sender not found");
    Validate.that(sender.isSuperAdmin(), Validate.isTrue(), "Sender is not a super admin");

    User user = userService.getUserByUsername(request.getUsername());
    Validate.that(user, Validate.isNotNull(), "User not found");
    Validate.that(user.isAdmin(), Validate.isTrue(), "User is not an admin");

    user.setAdmin(false);
    userService.updateUserCredentials(user);
  }
}
