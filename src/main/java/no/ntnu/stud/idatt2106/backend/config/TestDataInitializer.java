package no.ntnu.stud.idatt2106.backend.config;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import no.ntnu.stud.idatt2106.backend.service.AuthService;
import no.ntnu.stud.idatt2106.backend.service.EventService;
import no.ntnu.stud.idatt2106.backend.model.request.EventRequest;
import no.ntnu.stud.idatt2106.backend.model.request.LoginRequest;
import no.ntnu.stud.idatt2106.backend.model.request.RegisterRequest;

@Component
@Profile("test-e2e")
public class TestDataInitializer implements CommandLineRunner {

  private final AuthService authService;
  private final EventService eventService;


  @Autowired
  public TestDataInitializer(AuthService authService, EventService eventService) {
    this.authService = authService;
    this.eventService = eventService;
  }

  @Override
  public void run(String... args) throws Exception {
    initializeTestData();
  }

  /**
   * .
   * Initializes test data for the application
   */
  public void initializeTestData() {
  
    RegisterRequest testUserRequest = new RegisterRequest("ikkeAdmin",
         "Password12345", "testme@gmail.com");
    RegisterRequest adminUserRequest = new RegisterRequest("admin",
        "Password12345", "admin@tgmail.com");

    authService.register(testUserRequest);
    authService.register(adminUserRequest);
    
  }
}
