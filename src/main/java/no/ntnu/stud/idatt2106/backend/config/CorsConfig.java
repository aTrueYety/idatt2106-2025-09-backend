package no.ntnu.stud.idatt2106.backend.config;

import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * This class configures CORS (Cross-Origin Resource Sharing) settings for the
 * application.
 */
@Configuration
public class CorsConfig {

  @Value("${cors.allowed-origins:http://localhost:3000,http://localhost:4000}")
  private String allowedOrigins;

  /**
   * Configures CORS settings for the application.
   * This method allows requests from specific origins and methods.
   *
   * @return a CorsFilter object with the specified configuration
   */
  @Bean
  public CorsFilter corsFilter() {
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowCredentials(true);

    // Extract allowed origins from the application properties
    List<String> origins = Arrays.asList(allowedOrigins.split(","));
    config.setAllowedOrigins(origins);

    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
    config.setAllowedHeaders(List.of("Authorization", "Content-Type"));

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);

    return new CorsFilter(source);
  }
}