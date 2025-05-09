package no.ntnu.stud.idatt2106.backend.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security configs for tests.
 */
@TestConfiguration
public class SecurityConfigTest {

  /**
   * Configures the security filter chain for the tests.
   *
   * @param http the HTTP object to configure
   * @return the configured SecurityFilterChain
   * @throws Exception if an error occurs during configuration
   */
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> auth.anyRequest()
            .permitAll());
    return http.build();
  }
}
