package no.ntnu.stud.idatt2106.backend.service;

import no.ntnu.stud.idatt2106.backend.model.base.GroupHousehold;
import no.ntnu.stud.idatt2106.backend.model.base.User;
import no.ntnu.stud.idatt2106.backend.repository.GroupHouseholdRepository;
import no.ntnu.stud.idatt2106.backend.repository.HouseholdRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Contains tests for the GroupHouseholdService class.
 */
@ExtendWith(MockitoExtension.class)
public class GroupHouseholdServiceTests {
  
  @InjectMocks
  private GroupHouseholdService groupHouseholdService;

  @Mock
  private UserService userService;

  @Mock
  private GroupInviteService groupInviteService;

  @Mock
  private EmergencyGroupService emergencyGroupService;

  @Mock
  private JwtService jwtService;

  @Mock
  private GroupHouseholdRepository groupHouseholdRepository;

  @Mock
  private HouseholdRepository householdRepository;

  @Nested
  class DeleteTests {

    @Test
    void shouldReturnTrueOnSuccess() {
      Long userId = 1L;
      Long householdId = 5L;
      User user = new User();
      user.setId(userId);
      user.setHouseholdId(householdId);

      String token = "Bearer jwt.token";
      when(jwtService.extractUserId(token.substring(7))).thenReturn(userId);
      when(userService.getUserById(userId)).thenReturn(user);

      Long groupHouseholdId = 2L;
      Long groupId = 9L;
      GroupHousehold groupHousehold = new GroupHousehold();
      groupHousehold.setId(groupHouseholdId);
      groupHousehold.setGroupId(groupId);
      groupHousehold.setHouseholdId(householdId);

      when(groupHouseholdRepository.findById(groupHouseholdId))
          .thenReturn(Optional.of(groupHousehold));
      when(groupHouseholdRepository.findByHouseholdIdAndGroupId(5L, 9L)).thenReturn(groupHousehold);
      when(groupHouseholdRepository.deleteById(2L)).thenReturn(true);
      when(groupHouseholdRepository.findByGroupId(9L)).thenReturn(List.of());

      boolean result = groupHouseholdService.delete(2L, token);

      assertEquals(true, result);
      verify(groupHouseholdRepository).deleteById(groupHouseholdId);
    }
  }
}
