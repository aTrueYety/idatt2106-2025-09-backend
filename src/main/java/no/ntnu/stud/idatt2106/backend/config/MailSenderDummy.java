package no.ntnu.stud.idatt2106.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 * Dummy mail sender for testing purposes.
 * This class is used to create a dummy mail sender that does not send any
 * emails.
 * It is used in the test-e2e profile.
 */
@Configuration
@Profile("test-e2e")
public class MailSenderDummy {

  /**
   * Create a dummy mail sender that does not send any emails.
   *
   * @return a dummy mail sender
   */
  @Primary
  @Bean
  public JavaMailSender javaMailSender() {
    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
    mailSender.setHost("dummyHost");
    mailSender.setPort(0);
    return mailSender;
  }
}