package no.ntnu.stud.idatt2106.backend.config;

import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

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
      "/api/auth/login",
      "/ws",
      "/ws/**"
  };

  @Autowired
  private UserDetailsService userDetailsService;

  @Autowired
  private JwtAuthFilter jwtAuthFilter;

  @Value("${cors.allowed-origins:http://localhost:3000,http://localhost:4000}")
  private String allowedOrigins;

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
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
        .headers(headers -> headers
            .frameOptions(frameOptions -> frameOptions.sameOrigin()))
        .authorizeHttpRequests(auth -> auth
            // -- WHITELISTED ENDPOINTS --//
            .requestMatchers(AUTH_WHITELIST).permitAll()

            // -- EMERGENCY GROUP CONTROLLER--//
            // Public access
            .requestMatchers(HttpMethod.GET, "/api/emergency-groups").permitAll()
            .requestMatchers(HttpMethod.GET, "/api/emergency-groups/*").permitAll()
            .requestMatchers(HttpMethod.GET, "/api/emergency-groups/summary").permitAll()
            .requestMatchers(HttpMethod.GET, "/api/emergency-groups/summary/*").permitAll()
            .requestMatchers(HttpMethod.GET, "/api/emergency-groups/summary/summary/group/*")
            .permitAll()

            // -- EVENT CONTROLLER --//
            // Admin
            .requestMatchers(HttpMethod.POST, "/api/events").hasRole("ADMIN")
            .requestMatchers(HttpMethod.POST, "/api/events/update").hasRole("ADMIN")
            .requestMatchers(HttpMethod.DELETE, "/api/events/*").hasRole("ADMIN")

            // Public
            .requestMatchers(HttpMethod.GET, "/api/events/*").permitAll()
            .requestMatchers(HttpMethod.GET, "/api/events/bounds").permitAll()

            // -- EXTRA RESIDENT CONTROLLER --//
            // Public
            .requestMatchers(HttpMethod.GET, "/api/extra-residents").permitAll()
            .requestMatchers(HttpMethod.GET, "/api/extra-residents/*").permitAll()

            // -- EXTRA RESIDENT TYPE CONTROLLER --//
            // Public
            .requestMatchers(HttpMethod.GET, "/api/extra-resident-types").permitAll()
            .requestMatchers(HttpMethod.GET, "/api/extra-resident-types/*").permitAll()

            // -- FOOD CONTROLLER --//
            // Public
            .requestMatchers(HttpMethod.GET, "/api/food").permitAll()
            .requestMatchers(HttpMethod.GET, "/api/food/*").permitAll()

            // -- FOOD TYPE CONTROLLER --//
            // Public
            .requestMatchers(HttpMethod.GET, "/api/food-types").permitAll()
            .requestMatchers(HttpMethod.GET, "/api/food-types/*").permitAll()
            .requestMatchers(HttpMethod.GET, "/api/food-types/search").permitAll()

            // -- HOUSEHOLD KIT CONTROLLER --//
            // Public
            .requestMatchers(HttpMethod.GET, "/api/household-kits/kit/*").permitAll()

            // --INFO PAGE CONTROLLER --//
            // Public
            .requestMatchers(HttpMethod.GET, "/api/info-page/**").permitAll()

            // Admin
            .requestMatchers(HttpMethod.POST, "/api/info-page").hasRole("ADMIN")
            .requestMatchers(HttpMethod.PUT, "/api/info-page").hasRole("ADMIN")
            .requestMatchers(HttpMethod.DELETE, "/api/info-page/*").hasRole("ADMIN")

            // -- KIT CONTROLLER --//
            // Public
            .requestMatchers(HttpMethod.GET, "/api/kits").permitAll()
            .requestMatchers(HttpMethod.GET, "/api/kits/*").permitAll()
            .requestMatchers(HttpMethod.GET, "/api/kits/search").permitAll()

            // Admin
            .requestMatchers(HttpMethod.POST, "/api/kits").hasRole("ADMIN")
            .requestMatchers(HttpMethod.PUT, "/api/kits/*").hasRole("ADMIN")
            .requestMatchers(HttpMethod.DELETE, "/api/kits/*").hasRole("ADMIN")

            // -- MAP OBJECT CONTROLLER --//
            // Public
            .requestMatchers(HttpMethod.GET, "/api/map-object/*").permitAll()
            .requestMatchers(HttpMethod.GET, "/api/map-object/bounds").permitAll()
            .requestMatchers(HttpMethod.GET, "/api/map-object/closest").permitAll()

            // ADMIN
            .requestMatchers(HttpMethod.POST, "/api/map-object").hasRole("ADMIN")
            .requestMatchers(HttpMethod.PUT, "/api/map-object").hasRole("ADMIN")
            .requestMatchers(HttpMethod.DELETE, "/api/map-object/*").hasRole("ADMIN")

            // -- MAP OBJECT TYPE CONTROLLER --//
            // Public
            .requestMatchers(HttpMethod.GET, "/api/map-object-type").permitAll()
            .requestMatchers(HttpMethod.GET, "/api/map-object-type/*").permitAll()

            // Admin
            .requestMatchers(HttpMethod.POST, "/api/map-object-type").hasRole("ADMIN")
            .requestMatchers(HttpMethod.PUT, "/api/map-object-type").hasRole("ADMIN")
            .requestMatchers(HttpMethod.DELETE, "/api/map-object-type/*").hasRole("ADMIN")

            // -- SEVERITY CONTROLLER --//
            // Public
            .requestMatchers(HttpMethod.GET, "/api/severity").permitAll()
            .requestMatchers(HttpMethod.GET, "/api/severity/*").permitAll()

            // Admin
            .requestMatchers(HttpMethod.POST, "/api/severity").hasRole("ADMIN")
            .requestMatchers(HttpMethod.POST, "/api/severity/update").hasRole("ADMIN")
            .requestMatchers(HttpMethod.DELETE, "/api/severity/*").hasRole("ADMIN")

            // -- USER CONTROLLER --//
            // Super admin
            .requestMatchers(HttpMethod.GET, "/api/user/admins").hasRole("SUPERADMIN")
            .requestMatchers(HttpMethod.GET, "/api/user/pending-admins").hasRole("SUPERADMIN")

            // Public
            .requestMatchers(HttpMethod.GET, "/api/user/*").permitAll()
            .requestMatchers(HttpMethod.POST, "/api/user/confirm-email/*").permitAll()

            .anyRequest().authenticated())
        .sessionManagement(session -> {
          session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        })
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }

  /**
   * Exposes the CORS configuration source for the application.
   */
  @Bean
  public UrlBasedCorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowCredentials(true);
    config.setAllowedOrigins(Arrays.asList(allowedOrigins.split(",")));
    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
    
    // Add necessary headers for WebSocket support
    config.setAllowedHeaders(List.of(
        "Authorization", 
        "Content-Type",
        "Sec-WebSocket-Protocol",
        "Sec-WebSocket-Key", 
        "Sec-WebSocket-Version", 
        "Sec-WebSocket-Extensions",
        "Upgrade",
        "Connection"
    ));
    
    // Allow the headers to be exposed to the client
    config.setExposedHeaders(List.of(
        "Sec-WebSocket-Accept",
        "Upgrade",
        "Connection"
    ));

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);
    return source;
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