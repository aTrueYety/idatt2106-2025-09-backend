package no.ntnu.stud.idatt2106.backend.service;

import no.ntnu.stud.idatt2106.backend.model.base.User;
import no.ntnu.stud.idatt2106.backend.model.request.LoginRequest;
import no.ntnu.stud.idatt2106.backend.model.request.RegisterRequest;
import no.ntnu.stud.idatt2106.backend.model.response.ChangeCredentialsResponse;
import no.ntnu.stud.idatt2106.backend.model.response.LoginResponse;
import no.ntnu.stud.idatt2106.backend.model.response.RegisterResponse;
import no.ntnu.stud.idatt2106.backend.model.update.CredentialsUpdate;
import no.ntnu.stud.idatt2106.backend.util.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service class for handling auth-related operations such as registration and login.
 */
@Service
public class AuthService {

  @Autowired
  private JwtService jwtService;

  @Autowired
  AuthenticationManager authManager;

  @Autowired
  private UserService userService;

  private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);


  /**
   * Registers a new user in the system.
   */
  public RegisterResponse register(RegisterRequest registerRequest) {

    Validate.that(registerRequest.getUsername(),
        Validate.isNotBlankOrNull(), "Username cannot be blank or null"
    );
    Validate.that(userService.getUserByUsername(registerRequest.getUsername()),
        Validate.isNull(), "Username is not available"
    );

    Validate.that(registerRequest.getPassword(),
        Validate.isNotBlankOrNull(),
        "New password cannot be blank or null");
    Validate.that(isPasswordValid(registerRequest.getPassword()),
        Validate.isTrue(),
        "New password must be at least 8 characters long, including both a letter and a digit"
    );

    Validate.that(registerRequest.getEmail().matches(
            "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"),
        Validate.isTrue(), "Email is not valid"
    );
    Validate.that(userService.getUserByEmail(registerRequest.getEmail()),
        Validate.isNull(), "Email is already in use"
    );

    User user = new User();

    user.setUsername(registerRequest.getUsername());
    user.setPassword(encoder.encode(registerRequest.getPassword()));
    user.setEmail(registerRequest.getEmail());
    userService.addUser(user);

    String token = authenticateUser(registerRequest.getUsername(), registerRequest.getPassword());

    return new RegisterResponse("Registration successful!", token);
  }

  /**
   * Verifies the login credentials of a user.
   */
  public LoginResponse login(LoginRequest loginRequest) {
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
   *                                 along with desired username
   * @return response containing feedback message and new token
   */
  public ChangeCredentialsResponse changeCredentials(
      CredentialsUpdate credentialsUpdate,
      String token
  ) {
    User user = userService.getUserByUsername(
        jwtService.extractUserName(token.substring(7))
    );
    Validate.that(credentialsUpdate.getUsername(),
        Validate.isNotBlankOrNull(),
        "New username cannot be blank or null"
    );
    // check if new username is availanle if the request includes a new username,
    // skip if user is keeping old username
    if (!user.getUsername().equals(credentialsUpdate.getUsername())) {
      Validate.that(userService.getUserByUsername(credentialsUpdate.getUsername()),
          Validate.isNull(),
          "New username is not available"
      );
    }
    Validate.that(!encoder.matches(credentialsUpdate.getCurrentPassword(),
            user.getPassword()),
            Validate.isFalse(),
        "Current password is incorrect"
    );
    Validate.that(credentialsUpdate.getNewPassword(),
        Validate.isNotBlankOrNull(),
        "New password cannot be blank or null");
    Validate.that(isPasswordValid(credentialsUpdate.getNewPassword()),
        Validate.isTrue(),
        "New password must be at least 8 characters long, including both a letter and a digit"
    );
    Validate.that(
        credentialsUpdate.getCurrentPassword()
            .equals(credentialsUpdate.getNewPassword()),
        Validate.isFalse(),
        "New password cannot be the same as current password"
    );


    // saving the new credentials
    user.setPassword(encoder.encode(credentialsUpdate.getNewPassword()));
    user.setUsername(credentialsUpdate.getUsername());
    userService.updateUserCredentials(user);

    String newToken = authenticateUser(
        credentialsUpdate.getUsername(),
        credentialsUpdate.getNewPassword()
    );
    return new ChangeCredentialsResponse("Password changed successfully", newToken);
  }

  /**
   * Verifies that a password is valid.
   *
   * @param password password to check validity of
   * @return true if the password is valid,
   *         meaning it is atleast 8 characters long and contains both a letter and digit,
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
}
