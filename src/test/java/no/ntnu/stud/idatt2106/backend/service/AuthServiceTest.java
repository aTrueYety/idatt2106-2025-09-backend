package no.ntnu.stud.idatt2106.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import no.ntnu.stud.idatt2106.backend.model.base.User;
import no.ntnu.stud.idatt2106.backend.model.request.LoginRequest;
import no.ntnu.stud.idatt2106.backend.model.request.RegisterRequest;
import no.ntnu.stud.idatt2106.backend.model.response.ChangeCredentialsResponse;
import no.ntnu.stud.idatt2106.backend.model.response.LoginResponse;
import no.ntnu.stud.idatt2106.backend.model.response.RegisterResponse;
import no.ntnu.stud.idatt2106.backend.model.update.CredentialsUpdate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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
  private UserService userService;
  @Mock
  private EmailService emailService;

  private static final Logger logger = LoggerFactory.getLogger(AuthServiceTest.class);

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
    logger.info("Response: Test {}", response.getToken());
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

}
