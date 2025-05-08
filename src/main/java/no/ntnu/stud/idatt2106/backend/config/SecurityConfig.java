package no.ntnu.stud.idatt2106.backend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Security configuration class for the application.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private static final String[] AUTH_WHITELIST = {
    "/swagger-ui/**",
    "/v3/api-docs/**",
    "/api/auth/register",
    "/api/auth/login"
  };

  @Autowired
  private UserDetailsService userDetailsService;

  @Autowired
  private JwtAuthFilter jwtAuthFilter;

  /**
   * Configures the security filter chain for the application.
   *
   * @param http the HttpSecurity object to configure
   * @return the configured SecurityFilterChain
   * @throws Exception if an error occurs during configuration
   */
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> auth
            //-- WHITELISTED ENDPOINTS --//
            .requestMatchers(AUTH_WHITELIST).permitAll()

            //-- EMERGENCY GROUP --//
            .requestMatchers(HttpMethod.GET, "/api/emergency-groups").permitAll()
            .requestMatchers(HttpMethod.GET, "/api/emergency-groups/*").permitAll()
            .requestMatchers(HttpMethod.GET, "/api/emergency-groups/summary").permitAll()
            .requestMatchers(HttpMethod.GET, "/api/emergency-groups/summary/*").permitAll()
            .requestMatchers(HttpMethod.GET, "/api/emergency-groups/summary/summary/group/*")
                .permitAll()

            .anyRequest().authenticated()
        )
        .sessionManagement(session -> {
          session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        })
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }

  /**
   * Creates an AuthenticationProvider bean that uses a DaoAuthenticationProvider
   * with a BCryptPasswordEncoder for password encoding and a UserDetailsService.
   */
  @Bean
  public AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setPasswordEncoder(new BCryptPasswordEncoder(12));
    provider.setUserDetailsService(userDetailsService);

    return provider;
  }

  /**
   * Creates a UserDetailsService bean that uses the custom MyUserDetailsService.
   *
   * @return the UserDetailsService bean
   */
  @Bean
  public AuthenticationManager authenticationManager(
      AuthenticationConfiguration authenticationConfiguration)
      throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }
}
