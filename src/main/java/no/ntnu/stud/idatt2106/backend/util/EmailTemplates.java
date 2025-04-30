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
        + "<p><a href='http://localhost:3000/household/join/" 
        + inviteKey + "'>Bli med i husholdningen</a></p>"
        + "<p>Alternativt kan du skrive inn denne koden under hustand siden på Krisfeiker:</p>"
        + "<p>" + inviteKey + "</p>"
        + "<p>Hvis du ikke kjenner til denne invitasjonen, vennligst ignorer denne e-posten.</p>"
        + "</body></html>";
  }

  /**
   * Generates an email template for resetting a password.
   *
   * @param resetKey the link to reset the password
   * @return the password reset email template as a String
   */
  public static String getPasswordResetTemplate(String resetKey) {
    return "<html><body>"
      + "<h1>Tilbakestill passord</h1>"
      + "<p>Vennligst klikk på lenken nedenfor for å tilbakestille passordet ditt:</p>"
      + "<p><a href='http://localhost:3000/reset-password/" + resetKey + "'>Tilbakestill passord</a></p>"
      + "<p>Hvis du ikke ba om dette, vennligst ignorer denne e-posten.</p>"
      + "</body></html>";
  }
}