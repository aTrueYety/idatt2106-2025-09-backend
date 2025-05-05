package no.ntnu.stud.idatt2106.backend.service;

import no.ntnu.stud.idatt2106.backend.model.base.Household;
import no.ntnu.stud.idatt2106.backend.model.base.HouseholdInvite;
import no.ntnu.stud.idatt2106.backend.model.base.User;
import no.ntnu.stud.idatt2106.backend.model.response.HouseholdResponse;
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
  private UserService userService;
  @Autowired
  private HouseholdService householdService;

  /**
   * Generates a unique invite key for a household invite.
   *
   * @return a unique invite key as a string
   */
  private String generateInviteKey() {
    return java.util.UUID.randomUUID().toString();
  }

  /**
   * Creates a new household invite.
   *
   * @param householdId the ID of the household to invite to
   * @param userId      the ID of the user to invite
   * @return the generated invite key
   */
  public String createHouseholdInvite(Long householdId, Long userId) {
    Validate.that(householdId, Validate.isNotNull(), "Household ID cannot be null");
    Validate.that(userId, Validate.isNotNull(), "User ID cannot be null");
    Validate.that(repository.findByUserIdAndHouseholdId(userId, householdId),
        Validate.isEmptyCollection(), "User already has an invite to this household");

    User user = userService.getUserById(userId);
    Validate.that(user, Validate.isNotNull(), "User does not exist");
    Validate.that(user.getHouseholdId() == householdId,
        Validate.isTrue(), "User already belongs to this household");
    Validate.that(householdService.getById(householdId), Validate.isNotNull(),
        "Household does not exist");
     
    String inviteKey = generateInviteKey();
    HouseholdInvite invite = new HouseholdInvite(userId, householdId, inviteKey);
    repository.save(invite);
    return inviteKey;
  }

  /**
   * Finds a household invite by its key.
   *
   * @param key the invite key to search for
   * @return the found HouseholdInvite object, or null if not found
   */
  public HouseholdInvite findByKey(String key) {
    Validate.that(key, Validate.isNotNull(), "Invite key cannot be null");
    return repository.findByKey(key);
  }

  /**
   * Deletes a household invite by its key.
   *
   * @param key the invite key to delete
   */
  public void deleteInvite(String key) {
    Validate.that(key, Validate.isNotNull(), "Invite key cannot be null");
    repository.delete(key);
  }
}
