package no.ntnu.stud.idatt2106.backend.util;

/**
 * Utility class for generating email templates.
 */
public class EmailTemplates {

  /**
   * Generates a welcome email template.
   *
   * @param username the username of the recipient
   * @return the welcome email template as a String
   */
  public static String getWelcomeEmailTemplate(String username) {
    return "<html><body>"
        + "<h1>Welcome, " + username + "!</h1>"
        + "<p>Thank you for joining our platform.</p>"
        + "<p>If you have any questions, please contact our support team.</p>"
        + "</body></html>";
  }

  /**
   * Generates a password reset email template.
   *
   * @param resetLink the link to reset the password
   * @return the password reset email template as a String
   */
  public static String getPasswordResetTemplate(String resetLink) {
    return "<html><body>"
        + "<h1>Password Reset Request</h1>"
        + "<p>Please click the link below to reset your password:</p>"
        + "<p><a href='" + resetLink + "'>Reset Password</a></p>"
        + "<p>If you didn't request this, please ignore this email.</p>"
        + "</body></html>";
  }
}