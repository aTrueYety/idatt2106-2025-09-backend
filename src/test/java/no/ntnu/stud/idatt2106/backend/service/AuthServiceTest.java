package no.ntnu.stud.idatt2106.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.mail.MessagingException;
import no.ntnu.stud.idatt2106.backend.model.base.PasswordResetKey;
import no.ntnu.stud.idatt2106.backend.model.base.User;
import no.ntnu.stud.idatt2106.backend.model.request.LoginRequest;
import no.ntnu.stud.idatt2106.backend.model.request.PasswordResetKeyRequest;
import no.ntnu.stud.idatt2106.backend.model.request.PasswordResetRequest;
import no.ntnu.stud.idatt2106.backend.model.request.RegisterRequest;
import no.ntnu.stud.idatt2106.backend.model.response.ChangeCredentialsResponse;
import no.ntnu.stud.idatt2106.backend.model.response.LoginResponse;
import no.ntnu.stud.idatt2106.backend.model.response.RegisterResponse;
import no.ntnu.stud.idatt2106.backend.model.update.CredentialsUpdate;
import no.ntnu.stud.idatt2106.backend.util.EmailTemplates;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * Unit tests for the AuthService class.
 */
@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

  @Mock
  private JwtService jwtService;

  @Mock
  private AuthenticationManager authManager;

  @Mock
  private Authentication authentication;

  @Mock
  private UserService userService;

  @Mock
  private EmailService emailService;

  @Mock
  private PasswordResetService passwordResetKeyService;

  @Mock
  private BCryptPasswordEncoder encoder;

  @InjectMocks
  private AuthService authService;

  @Test
  public void testRegisterSuccess() {

    User user = new User();
    user.setId(1L);
    user.setUsername("newUser");
    user.setAdmin(false);
    user.setSuperAdmin(false);

    when(userService.getUserByUsername("newUser")).thenReturn(null).thenReturn(user);
    when(userService.getUserByEmail("new@example.com")).thenReturn(null);
    Authentication auth = mock(Authentication.class);
    when(auth.isAuthenticated()).thenReturn(true);
    when(authManager.authenticate(any())).thenReturn(auth);

    when(jwtService.generateToken(anyString(), anyLong(), anyBoolean(), anyBoolean()))
        .thenReturn("token123");
    RegisterRequest request = new RegisterRequest("newUser",
        "Password123", "new@example.com");
    RegisterResponse response = authService.register(request);
    assertEquals("Registration successful!", response.getMessage());
    assertEquals("token123", response.getToken());
  }

  @Test
  public void testLoginSuccess() {

    User user = new User();
    user.setId(1L);
    user.setUsername("username");
    user.setPassword("Password123");
    user.setAdmin(true);
    user.setSuperAdmin(false);

    Authentication authentication = mock(Authentication.class);
    when(authentication.isAuthenticated()).thenReturn(true);

    when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
        .thenReturn(authentication);
    when(userService.getUserByUsername("username")).thenReturn(user);
    when(jwtService.generateToken("username", 1L, true, false)).thenReturn("token456");

    LoginRequest loginRequest = new LoginRequest("username", "Password123");
    LoginResponse response = authService.login(loginRequest);

    assertEquals("Login successful!", response.getMessage());
    assertEquals("token456", response.getToken());
  }

  @Test
  public void testRegisterFailsWhenUsernameExists() {

    when(userService.getUserByUsername("existingUser")).thenReturn(new User());

    RegisterRequest request = new RegisterRequest("existingUser",
        "Password123", "email@example.com");

    try {
      authService.register(request);
    } catch (IllegalArgumentException e) {
      assertEquals("Username is not available", e.getMessage());
    }
  }

  @Test
  public void testRegisterFailsWhenEmailExists() {

    when(userService.getUserByEmail("existing@example.com")).thenReturn(new User());

    RegisterRequest request = new RegisterRequest("newUser", "Password123", "existing@example.com");

    try {
      authService.register(request);
    } catch (IllegalArgumentException e) {
      assertEquals("Email is already in use", e.getMessage());
    }
  }

  @Test
  public void testLoginFailsWithInvalidCredentials() {

    when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
        .thenThrow(new RuntimeException("Invalid credentials"));

    LoginRequest loginRequest = new LoginRequest("username", "wrongPassword");

    try {
      authService.login(loginRequest);
    } catch (RuntimeException e) {
      assertEquals("Invalid credentials", e.getMessage());
    }
  }

  @Test
  public void testValidateTokenSuccess() {

    when(jwtService.extractUserName(anyString())).thenReturn("username");

    String token = "Bearer validToken123";

    try {
      authService.validateToken(token);
    } catch (Exception e) {
      assertNotNull(null, "Token validation should not throw an exception");
    }
  }

  @Test
  void testRegister_throwsRuntimeException_whenEmailFails() throws MessagingException {

    when(userService.getUserByUsername(anyString())).thenReturn(null);
    when(userService.getUserByEmail(anyString())).thenReturn(null);
    when(authManager.authenticate(any())).thenReturn(mock(Authentication.class));

    doThrow(new MessagingException("Simulated failure"))
        .when(emailService)
        .sendHtmlEmail(anyString(), anyString(), anyString());

    RegisterRequest request = new RegisterRequest("testuser",
        "SterktPassord1", "jacoblein@gmail.com");

    // Act & Assert
    RuntimeException exception = assertThrows(RuntimeException.class, () -> {
      authService.register(request);
    });
    assertEquals("Failed to send welcome email", exception.getMessage());
  }

  @Test
  void testRegister_throwsExceptionWhenPasswordIsBadFormat() throws MessagingException {

    when(userService.getUserByUsername(anyString())).thenReturn(null);

    RegisterRequest request = new RegisterRequest("testuser",
        "SterktPassord", "jacoblein@gmail.com");

    // Act & Assert
    RuntimeException exception = assertThrows(RuntimeException.class, () -> {
      authService.register(request);
    });
    assertEquals(
        "New password must be at least 8 characters long, including both a letter and a digit",
        exception.getMessage());
  }

  @Test
  void testRegister_throwsExceptionWhenEmailIsBadFormat() throws MessagingException {

    when(userService.getUserByUsername(anyString())).thenReturn(null);

    RegisterRequest request = new RegisterRequest("testuser",
        "SterktPassord1", "jacoblein@gmail");

    // Act & Assert
    RuntimeException exception = assertThrows(RuntimeException.class, () -> {
      authService.register(request);
    });
    assertEquals("Email is not valid", exception.getMessage());
  }

  @Test
  void testRegister_throwsExceptionWhenEmailMissesAt() throws MessagingException {

    when(userService.getUserByUsername(anyString())).thenReturn(null);

    RegisterRequest request = new RegisterRequest("testuser",
        "SterktPassord1", "jacobleingmail.com");

    // Act & Assert
    RuntimeException exception = assertThrows(RuntimeException.class, () -> {
      authService.register(request);
    });
    assertEquals("Email is not valid", exception.getMessage());
  }

  @Test
  public void testChangeCredentials_throwsExceptionWhenUsernameNotAvailable() {

    User currentUser = new User();
    currentUser.setId(1L);
    currentUser.setUsername("currentUser");
    currentUser.setPassword("OldPassword123");
    currentUser.setEmail("jacoblein2010@gmail.com");

    User existingUser = new User();
    existingUser.setUsername("existingUser");

    CredentialsUpdate update = new CredentialsUpdate();
    update.setUsername("existingUser");
    update.setCurrentPassword("OldPassword123");
    update.setNewPassword("NewPassword123");

    when(jwtService.extractUserName("token")).thenReturn("currentUser");
    when(userService.getUserByUsername("currentUser")).thenReturn(currentUser);
    when(userService.getUserByUsername("existingUser")).thenReturn(existingUser);

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      authService.changeCredentials(update, "Bearer token");
    });
    assertEquals("New username is not available", exception.getMessage());
  }

  @Test
  public void testChangeCredentials_throwsExceptionWhenCurrentPasswordIsWrong() {

    User currentUser = new User();
    currentUser.setId(1L);
    currentUser.setUsername("currentUser");
    currentUser.setPassword("OldPassword123");
    currentUser.setEmail("jacoblein2010@gmail.com");

    CredentialsUpdate update = new CredentialsUpdate();
    update.setUsername("newName");
    update.setCurrentPassword("wrongPassword");
    update.setNewPassword("NewPassword123");

    when(jwtService.extractUserName("token")).thenReturn("currentUser");
    when(userService.getUserByUsername("currentUser")).thenReturn(currentUser);

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      authService.changeCredentials(update, "Bearer token");
    });
    assertEquals("Current password is incorrect", exception.getMessage());
  }

  @Test
  public void testChangeCredentials_throwsExceptionWhenNewPasswordIsBadFormat() {
    ReflectionTestUtils.setField(authService, "encoder", encoder);

    when(jwtService.extractUserName("token")).thenReturn("currentUser");
    User currentUser = new User();
    currentUser.setId(1L);
    currentUser.setUsername("currentUser");
    currentUser.setPassword("encodedOldPassword");
    when(userService.getUserByUsername("currentUser")).thenReturn(currentUser);
    when(userService.getUserByUsername("newName")).thenReturn(null);

    when(encoder.matches("OldPassword123", "encodedOldPassword")).thenReturn(true);

    CredentialsUpdate update = new CredentialsUpdate();
    update.setUsername("newName");
    update.setCurrentPassword("OldPassword123");
    update.setNewPassword("bad");

    IllegalArgumentException ex = assertThrows(
        IllegalArgumentException.class,
        () -> authService.changeCredentials(update, "Bearer token"));

    assertEquals(
        "New password must be at least 8 characters long, including both a letter and a digit",
        ex.getMessage());
  }

  @Test
  public void testChangeCredentialsSuccess() {
    // Replace the real encoder with our mock so stubbing actually applies
    ReflectionTestUtils.setField(authService, "encoder", encoder);

    // Stub JWT lookup + original-user lookup
    when(jwtService.extractUserName("token")).thenReturn("oldUser");
    User user = new User();
    user.setId(1L);
    user.setUsername("oldUser");
    user.setPassword("encodedOldPass123");
    when(userService.getUserByUsername("oldUser")).thenReturn(user);

    // Then, after service has set user.username="newUser", lookup returns user
    when(userService.getUserByUsername("newUser"))
        .thenReturn(null)
        .thenReturn(user);

    // Current-password check passes
    when(encoder.matches("oldPassword123", "encodedOldPass123")).thenReturn(true);

    // Encoding the new password
    when(encoder.encode("newPassword123")).thenReturn("encodedNewPass123");

    // Authentication for authenticateUser
    Authentication auth = mock(Authentication.class);
    when(auth.isAuthenticated()).thenReturn(true);
    when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
        .thenReturn(auth);

    // Token generation stub
    when(jwtService.generateToken("newUser", 1L, false, false))
        .thenReturn("fakeToken123");

    // Perform the call
    CredentialsUpdate update = new CredentialsUpdate();
    update.setUsername("newUser");
    update.setCurrentPassword("oldPassword123");
    update.setNewPassword("newPassword123");
    ChangeCredentialsResponse resp = authService.changeCredentials(update, "Bearer token");
    // Verify the results
    assertEquals("Password changed successfully", resp.getMessage());
    assertEquals("fakeToken123", resp.getToken());
    verify(userService).updateUserCredentials(argThat(u -> u.getId().equals(1L) &&
        "newUser".equals(u.getUsername()) &&
        "encodedNewPass123".equals(u.getPassword())));
  }

  @Test
  public void testRegister_throwsExceptionWhenUsernameIsNull() {
    RegisterRequest request = new RegisterRequest(null, "Password123", "email@example.com");

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      authService.register(request);
    });
    assertEquals("Username cannot be blank or null", exception.getMessage());
  }

  @Test
  public void testRegister_throwsExceptionWhenPasswordIsNull() {
    RegisterRequest request = new RegisterRequest("testuser", null, "email@example.com");

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      authService.register(request);
    });
    assertEquals("New password cannot be blank or null", exception.getMessage());
  }

  @Test
  public void testRegister_throwsExceptionWhenEmailIsNull() {
    RegisterRequest request = new RegisterRequest("testuser", "Password123", "null");

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      authService.register(request);
    });
    assertEquals("Email is not valid", exception.getMessage());
  }

  @Test
  public void testLogin_throwsExceptionWhenUsernameIsNull() {
    LoginRequest loginRequest = new LoginRequest(null, "Password123");

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      authService.login(loginRequest);
    });
    assertEquals("Username cannot be null or empty", exception.getMessage());
  }

  @Test
  public void testLogin_throwsExceptionWhenPasswordIsNull() {
    LoginRequest loginRequest = new LoginRequest("username", null);

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      authService.login(loginRequest);
    });
    assertEquals("Password cannot be null or empty", exception.getMessage());
  }

  @Test
  public void testValidateToken_throwsExceptionWhenTokenIsNull() {
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      authService.validateToken(null);
    });
    assertEquals("Token cannot be null or empty", exception.getMessage());
  }

  @Test
  public void testValidateToken_throwsExceptionWhenTokenIsInvalid() {
    when(jwtService.extractUserName("invalidToken"))
        .thenThrow(new RuntimeException("Invalid token"));

    RuntimeException exception = assertThrows(RuntimeException.class, () -> {
      authService.validateToken("Bearer invalidToken");
    });
    assertEquals("Invalid token", exception.getMessage());
  }

  @Test
  public void testResetPasswordSuccess() {

    String key = "reset-abc-123";
    PasswordResetKey prk = new PasswordResetKey();
    prk.setUserId(17L);
    User u = new User();
    u.setId(17L);

    when(passwordResetKeyService.findByKey(key)).thenReturn(prk);
    when(userService.getUserById(17L)).thenReturn(u);

    PasswordResetRequest req = new PasswordResetRequest(key, "NewPass123");
    authService.resetPassword(req);

    verify(userService).updateUserCredentials(argThat(updated -> updated.getId().equals(17L) &&
        updated.getPassword() != null &&
        !updated.getPassword().isEmpty()));

    verify(passwordResetKeyService).deletePasswordResetKey(key);
  }

  @Test
  public void testResetPassword_throwsWhenKeyNotFound() {
    when(passwordResetKeyService.findByKey("no-such-key")).thenReturn(null);

    PasswordResetRequest req = new PasswordResetRequest("no-such-key", "NewPass123");
    IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
      authService.resetPassword(req);
    });
    assertEquals("Key not found", ex.getMessage());
  }

  @Test
  public void testResetPassword_throwsWhenNewPasswordInvalid() {

    PasswordResetKey prk = new PasswordResetKey();
    prk.setUserId(5L);

    PasswordResetRequest req = new PasswordResetRequest("some-key", "short");
    IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
      authService.resetPassword(req);
    });
    assertEquals(
        "New password must be at least 8 characters long, including both a letter and a digit",
        ex.getMessage());
  }

  @Test
  public void testRequestPasswordResetSuccess() throws MessagingException {
    
    String email = "user@example.com";
    User user = new User();
    user.setId(42L);
    user.setEmail(email);
    when(userService.getUserByEmail(email)).thenReturn(user);
    String key = "reset-key-123";
    when(passwordResetKeyService.createPasswordResetKey(42L)).thenReturn(key);

    PasswordResetKeyRequest req = new PasswordResetKeyRequest();
    req.setEmail(email);

    authService.requestPasswordReset(req);

    verify(passwordResetKeyService).createPasswordResetKey(42L);

    String expectedHtml = EmailTemplates.getPasswordResetTemplate(key);
    verify(emailService).sendHtmlEmail(
        eq(email),
        eq("Tilbakestill passord"),
        eq(expectedHtml));
  }

  @Test
  public void testRequestPasswordResetThrowsWhenEmailNull() {
    PasswordResetKeyRequest req = new PasswordResetKeyRequest();
    req.setEmail(null);

    IllegalArgumentException ex = assertThrows(
        IllegalArgumentException.class,
        () -> authService.requestPasswordReset(req));
    assertEquals("Email cannot be blank or null", ex.getMessage());
  }

  @Test
  public void testRequestPasswordResetThrowsWhenEmailBadFormat() {
    PasswordResetKeyRequest req = new PasswordResetKeyRequest();
    req.setEmail("not-an-email");

    IllegalArgumentException ex = assertThrows(
        IllegalArgumentException.class,
        () -> authService.requestPasswordReset(req));
    assertEquals("Email is not valid", ex.getMessage());
  }

  @Test
  public void testRequestPasswordResetEmailSendFails() throws MessagingException {
  
    String email = "user@example.com";
    User user = new User();
    user.setId(99L);
    user.setEmail(email);
    when(userService.getUserByEmail(email)).thenReturn(user);
    when(passwordResetKeyService.createPasswordResetKey(99L)).thenReturn("any-key");

    doThrow(new MessagingException("SMTP down"))
        .when(emailService).sendHtmlEmail(anyString(), anyString(), anyString());

    PasswordResetKeyRequest req = new PasswordResetKeyRequest();
    req.setEmail(email);

    RuntimeException ex = assertThrows(
        RuntimeException.class,
        () -> authService.requestPasswordReset(req));
    assertEquals("Failed to send password reset email", ex.getMessage());
  }

}
