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

  /**
   * Generates an email template for inviting a user to a household.
   *
   * @param householdName the name of the household
   * @param inviteKey the link to accept the invitation
   * @return the invitation email template as a String
   */
  public static String getHouseholdInviteTemplate(String householdName, String inviteKey) {
    return "<html><body>"
        + "<h1>Du er invitert!</h1>"
        + "<p>Du har blitt invitert til å bli med i hustanden: " + householdName + "</p>"
        + "<p>Trykk på linken under for å akseptere invitasjonen:</p>"
        + "<p><a href='http://localhost:3000/households/join/" 
        + inviteKey + "'>Bli med i husholdningen</a></p>"
        + "<p>Alternativt kan du skrive inn denne koden under hustand siden på Krisfeiker:</p>"
        + "<p>" + inviteKey + "</p>"
        + "<p>Hvis du ikke kjenner til denne invitasjonen, vennligst ignorer denne e-posten.</p>"
        + "</body></html>";
  }
}