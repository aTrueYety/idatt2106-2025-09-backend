package no.ntnu.stud.idatt2106.backend.service;

import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * Service class for handling reCAPTCHA verification.
 * This class is responsible for verifying the reCAPTCHA token provided by the
 * user during registration.
 */
@Service
public class RecaptchaService {
  @Value("${recaptcha.secret}")
  private String recaptchaSecret;

  private static final String VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify";

  /**
   * Verifies the reCAPTCHA token provided by the user.
   *
   * @param token the reCAPTCHA token to verify
   * @return true if the token is valid, false otherwise
   */
  public boolean verifyToken(String token) {
    RestTemplate restTemplate = new RestTemplate();
    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("secret", recaptchaSecret);
    params.add("response", token);

    ResponseEntity<Map> response = restTemplate.postForEntity(
        VERIFY_URL, new HttpEntity<>(params), Map.class);

    return (Boolean) response.getBody().get("success");
  }
}
