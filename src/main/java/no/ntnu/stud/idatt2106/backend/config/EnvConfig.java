package no.ntnu.stud.idatt2106.backend.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for loading environment variables using Dotenv.
 * This class is responsible for creating a Dotenv bean that can be used throughout the application.
 */
@Configuration
public class EnvConfig {

  /**
   * Creates a Dotenv bean that loads environment variables from a .env file.
   * The file is ignored if it does not exist.
   *
   * @return the Dotenv bean
   */
  @Bean
  public Dotenv dotenv() {
    return Dotenv.configure()
        .filename(".env")
        .ignoreIfMissing()
        .load();
  }
}
