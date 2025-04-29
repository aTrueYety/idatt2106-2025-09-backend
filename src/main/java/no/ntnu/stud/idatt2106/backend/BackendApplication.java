package no.ntnu.stud.idatt2106.backend;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main class for the Spring Boot application.
 * This class is responsible for starting the application and loading environment variables.
 * It uses the Dotenv library to load environment variables from a .env file.
 */
@SpringBootApplication
public class BackendApplication {

  /**
   * Main method to run the Spring Boot application.
   * Loads environment variables from a .env file if it exists. 
   *
   * @param args Command line arguments
   */
  public static void main(String[] args) {
    Dotenv dotenv = Dotenv.configure()
        .filename(".env")
        .ignoreIfMissing()
        .load();

    dotenv.entries().forEach(entry -> {
      System.setProperty(entry.getKey(), entry.getValue());
    });

    SpringApplication.run(BackendApplication.class, args);
  }
}
