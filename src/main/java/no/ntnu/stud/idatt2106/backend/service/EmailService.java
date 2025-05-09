package no.ntnu.stud.idatt2106.backend.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

/**
 * Service class for sending emails.
 */
@Service
public class EmailService {

  @Autowired
  private JavaMailSender mailSender;

  @Value("${spring.mail.username}")
  private String fromEmail;

  @Value("${frontend.url}")
  private String frontendUrl;

  /**
   * Send a simple email.
   *
   * @param to to email address
   * @param subject subject of the email
   * @param text text of the email
   */
  public void sendSimpleEmail(String to, String subject, String text) {
    text = text.replace("{frontendUrl}", frontendUrl);

    SimpleMailMessage message = new SimpleMailMessage();
    message.setFrom(fromEmail);
    message.setTo(to);
    message.setSubject(subject);
    message.setText(text);

    mailSender.send(message);
  }

  /**
   * Send an HTML email.
   *
   * @param to to email address
   * @param subject subject of the email
   * @param htmlContent HTML content of the email
   * @throws MessagingException if an error occurs while sending the email
   */
  public void sendHtmlEmail(String to, String subject, String htmlContent) 
      throws MessagingException {
    htmlContent = htmlContent.replace("{frontendUrl}", frontendUrl);

    MimeMessage message = mailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

    helper.setFrom(fromEmail);
    helper.setTo(to);
    helper.setSubject(subject);
    helper.setText(htmlContent, true);

    mailSender.send(message);
  }
}