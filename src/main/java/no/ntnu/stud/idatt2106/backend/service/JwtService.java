package no.ntnu.stud.idatt2106.backend.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * Service class for handling JWT (JSON Web Token) operations.
 * This class provides methods to generate, validate, and extract information
 * from JWTs.
 */
@Service
public class JwtService {

  private String secretkey = "";

  /**
   * Constructor that initializes the secret key for signing JWTs.
   */
  public JwtService() {

    try {
      KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
      SecretKey sk = keyGen.generateKey();
      secretkey = Base64.getEncoder().encodeToString(sk.getEncoded());
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Generates a JWT token with the specified username, userId, and admin status.
   *
   * @param username the username to include in the token
   * @param userId   the user ID to include in the token
   * @param isAdmin  whether the user is an admin
   * @param isSuperAdmin whether the user is a super admin
   * @return the generated JWT token as a string
   */
  public String generateToken(String username, long userId, boolean isAdmin, boolean isSuperAdmin) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("refreshToken", "placeholder");
    claims.put("userId", userId);
    claims.put("isAdmin", isAdmin);
    claims.put("isSuperAdmin", isSuperAdmin);

    return Jwts.builder()
        .claims()
        .add(claims)
        .subject(username)
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(new Date(System.currentTimeMillis() + 1000 * 5 * 60 * 60))
        .and()
        .signWith(getKey())
        .compact();
  }

  private SecretKey getKey() {
    byte[] keyBytes = Decoders.BASE64.decode(secretkey);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  /**
   * Extracts the username from the given JWT token.
   *
   * @param token the JWT token to extract the username from
   * @return the username extracted from the token
   */
  public String extractUserName(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  /**
   * Extracts the user ID from the given JWT token.
   *
   * @param token the JWT token to extract the user ID from
   * @return the user ID extracted from the token
   */
  public Long extractUserId(String token) {
    return extractClaim(token, claims -> claims.get("userId", Long.class));
  }

  /**
   * Extracts the refresh token from the given JWT token.
   *
   * @param token the JWT token to extract the refresh token from
   * @return the refresh token extracted from the token
   */
  public boolean extractIsAdmin(String token) {
    return extractClaim(token, claims -> claims.get("isAdmin", Boolean.class));
  }

  /**
   * Extracts the refresh token from the given JWT token.
   *
   * @param token the JWT token to extract the refresh token from
   * @return the refresh token extracted from the token
   */
  public boolean extractIsSuperAdmin(String token) {
    return extractClaim(token, claims -> claims.get("isSuperAdmin", Boolean.class));
  }

  private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
    final Claims claims = extractAllClaims(token);
    return claimResolver.apply(claims);
  }

  private Claims extractAllClaims(String token) {
    return Jwts.parser()
        .verifyWith(getKey())
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }

  /**
   * Validates the given JWT token against the provided user details.
   *
   * @param token the JWT token to validate
   * @param userDetails the user details to validate against
   * @return true if the token is valid, false otherwise
   */
  public boolean validateToken(String token, UserDetails userDetails) {
    final String userName = extractUserName(token);
    return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
  }

  private boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  private Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }
}