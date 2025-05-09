package no.ntnu.stud.idatt2106.backend.service;

import java.util.List;
import no.ntnu.stud.idatt2106.backend.model.base.HouseholdInvite;
import no.ntnu.stud.idatt2106.backend.repository.HouseholdInviteRepositoryImpl;
import no.ntnu.stud.idatt2106.backend.util.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service class for managing household invites.
 */
@Service
public class HouseholdInviteService {
  @Autowired
  private HouseholdInviteRepositoryImpl repository;
  @Autowired
  private JwtService jwtService;

  /**
   * Creates a new household invite.
   *
   * @param householdId the ID of the household to invite to
   * @param userId      the ID of the user to invite
   */
  public void createHouseholdInvite(Long householdId, Long userId) {
    Validate.that(householdId, Validate.isNotNull(), "Household ID cannot be null");
    Validate.that(userId, Validate.isNotNull(), "User ID cannot be null");
    Validate.that(repository.findByUserIdAndHouseholdId(userId, householdId),
        Validate.isNull(), "User already has an invite to this household");

    HouseholdInvite invite = new HouseholdInvite(userId, householdId);
    repository.save(invite);
  }

  /**
   * Retrieves a household invite by its user ID and household ID.
   *
   * @param userId      the ID of the user
   * @param householdId the ID of the household
   * @return the household invite, or null if not found
   */
  public HouseholdInvite findByUserIdAndHouseholdId(Long userId, Long householdId) {
    Validate.that(userId, Validate.isNotNull(), "User ID cannot be null");
    Validate.that(householdId, Validate.isNotNull(), "Household ID cannot be null");
    return repository.findByUserIdAndHouseholdId(userId, householdId);
  }

  /**
   * Deletes a household invite by its user ID and household ID.
   *
   * @param userId      the ID of the user
   * @param householdId the ID of the household
   */
  public void deleteHouseholdInvite(Long userId, Long householdId) {
    Validate.that(userId, Validate.isNotNull(), "User ID cannot be null");
    Validate.that(householdId, Validate.isNotNull(), "Household ID cannot be null");
    repository.deleteByUserIdAndHouseholdId(userId, householdId);
  }

  /**
   * Retrives all household invites for a specific user.
   *
   * @param token the JWT token of the user
   */
  public List<HouseholdInvite> findHouseholdInvitesForUser(String token) {
    Validate.that(token, Validate.isNotNull(), "Token cannot be null");
    Long userId = jwtService.extractUserId(token.substring(7));
    return repository.findByUserId(userId);
  }

  /**
   * Retrieves all household invites for a specific household.
   *
   * @param token the JWT token of the user
   */
  public List<HouseholdInvite> findHouseholdInvitesForHousehold(String token) {
    Validate.that(token, Validate.isNotNull(), "Token cannot be null");
    Long userId = jwtService.extractUserId(token.substring(7));
    return repository.findByHouseholdId(userId);
  }
}
