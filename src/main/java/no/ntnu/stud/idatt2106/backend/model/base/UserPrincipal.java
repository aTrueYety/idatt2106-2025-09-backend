package no.ntnu.stud.idatt2106.backend.model.base;

import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Represents the principal user details for authentication and authorization.
 * This class implements the UserDetails interface provided by Spring Security.
 */
public class UserPrincipal implements UserDetails {
  private String username;
  private String password;
  private Collection<? extends GrantedAuthority> authorities;

  /**
   * Constructs a new UserPrincipal from a User object.
   *
   * @param user the User object
   */
  public UserPrincipal(User user) {
    this.username = user.getUsername();
    this.password = user.getPassword();

    SimpleGrantedAuthority userAuth = new SimpleGrantedAuthority("ROLE_USER");
    SimpleGrantedAuthority adminAuth = new SimpleGrantedAuthority("ROLE_ADMIN");
    SimpleGrantedAuthority superadminAuth = new SimpleGrantedAuthority("ROLE_SUPERADMIN");


    if (user.isSuperAdmin()) {
      this.authorities = List.of(userAuth, adminAuth, superadminAuth);
    } else if (user.isAdmin()) {
      this.authorities = List.of(userAuth, adminAuth);
    } else {
      this.authorities = List.of(userAuth);
    }

  }

  /**
   * Returns the authorities granted to the user.
   *
   * @return the authorities granted to the user
   */
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  /**
   * Returns the password used to authenticate the user.
   *
   * @return the password
   */
  @Override
  public String getPassword() {
    return password;
  }

  /**
   * Returns the username used to authenticate the user.
   *
   * @return the username
   */
  @Override
  public String getUsername() {
    return username;
  }

  /**
   * Indicates whether the user's account has expired.
   *
   * @return true if the user's account is valid (i.e., non-expired), false if no
   *         longer valid (i.e., expired)
   */
  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  /**
   * Indicates whether the user is locked or unlocked.
   *
   * @return true if the user is not locked, false otherwise
   */
  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  /**
   * Indicates whether the user's credentials (password) has expired.
   *
   * @return true if the user's credentials are valid (i.e., non-expired), false
   *         if no longer valid (i.e., expired)
   */
  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  /**
   * Indicates whether the user is enabled or disabled.
   *
   * @return true if the user is enabled, false otherwise
   */
  @Override
  public boolean isEnabled() {
    return true;
  }
}