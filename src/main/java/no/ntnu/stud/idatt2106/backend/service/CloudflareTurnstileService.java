package no.ntnu.stud.idatt2106.backend.service;

import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


/**
 * Service for verifying Cloudflare Turnstile CAPTCHA responses.
 * This service communicates with the Cloudflare Turnstile API to verify CAPTCHA
 * responses.
 */
@Service
public class CloudflareTurnstileService {

  @Value("${cloudflare.turnstile.secret-key}")
  private String secretKey;
  private static final String VERIFY_URL = 
      "https://challenges.cloudflare.com/turnstile/v0/siteverify";

  /**
   * Verifies the CAPTCHA response using the Cloudflare Turnstile API.
   *
   * @param captchaResponse the CAPTCHA response token to verify
   * @return a TurnstileVerificationResponse object containing the verification
   *         result
   */
  public boolean verifyCaptcha(String captchaResponse) {
    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", "application/x-www-form-urlencoded");
    
    MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
    requestBody.add("secret", secretKey);
    requestBody.add("response", captchaResponse);
    RestTemplate restTemplate = new RestTemplate();
    HttpEntity<MultiValueMap<String, String>> requestEntity = 
        new HttpEntity<>(requestBody, headers);

    ResponseEntity<Map> response = restTemplate.postForEntity(VERIFY_URL, requestEntity, Map.class);

    return response.getBody() != null && (Boolean) response.getBody().get("success");
  }
}