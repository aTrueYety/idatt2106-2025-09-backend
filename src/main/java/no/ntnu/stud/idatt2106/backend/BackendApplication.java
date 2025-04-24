package no.ntnu.stud.idatt2106.backend;

import io.github.cdimascio.dotenv.Dotenv;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BackendApplication {

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
