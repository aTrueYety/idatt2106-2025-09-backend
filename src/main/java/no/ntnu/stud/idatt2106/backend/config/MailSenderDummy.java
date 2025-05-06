package no.ntnu.stud.idatt2106.backend.config;

import jakarta.mail.internet.MimeMessage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;

import jakarta.mail.MessagingException;
import jakarta.mail.Session;

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
    return new JavaMailSender() {

      @Override
      public MimeMessage createMimeMessage() {
        // Return an empty MimeMessage
        return new MimeMessage((Session) null);
      }

      @Override
      public MimeMessage createMimeMessage(java.io.InputStream contentStream) {
        try {
          return new MimeMessage(null, contentStream);
        } catch (MessagingException e) {
          // For the dummy, wrap any exception in a RuntimeException
          throw new RuntimeException("Error creating dummy MimeMessage", e);
        }
      }

      @Override
      public void send(MimeMessage mimeMessage) {
        // no-op
      }

      @Override
      public void send(MimeMessage... mimeMessages) {
        // no-op
      }

      @Override
      public void send(MimeMessagePreparator mimeMessagePreparator) {
        // no-op
      }

      @Override
      public void send(MimeMessagePreparator... mimeMessagePreparators) {
        // no-op
      }

      @Override
      public void send(SimpleMailMessage simpleMessage) {
        // no-op
      }

      @Override
      public void send(SimpleMailMessage... simpleMessages) {
        // no-op
      }
    };
  }
}