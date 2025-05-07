package no.ntnu.stud.idatt2106.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import no.ntnu.stud.idatt2106.backend.mapper.GroupHouseholdMapper;
import no.ntnu.stud.idatt2106.backend.model.base.EmergencyGroup;
import no.ntnu.stud.idatt2106.backend.model.base.GroupHousehold;
import no.ntnu.stud.idatt2106.backend.model.base.User;
import no.ntnu.stud.idatt2106.backend.model.request.GroupHouseholdRequest;
import no.ntnu.stud.idatt2106.backend.repository.GroupHouseholdRepository;
import no.ntnu.stud.idatt2106.backend.repository.HouseholdRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
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

  @Nested
  class InviteTests {

    @Test
    void shouldCreateGroupInvite() {
      Long householdId = 2L;
      Long groupId = 5L;
      GroupHouseholdRequest request = new GroupHouseholdRequest();
      request.setGroupId(groupId);
      request.setHouseholdId(householdId);
      String token = "Bearer token";

      when(groupHouseholdRepository.findByHouseholdIdAndGroupId(2L, 5L)).thenReturn(null);
      when(groupInviteService.hasGroupInvite(2L, 5L)).thenReturn(false);

      Long userId = 6L;
      when(jwtService.extractUserId(token.substring(7))).thenReturn(userId);

      User user = new User();
      user.setId(userId);
      user.setHouseholdId(4L);

      GroupHousehold groupHousehold = new GroupHousehold();
      groupHousehold.setHouseholdId(4L);
      groupHousehold.setGroupId(5L);

      when(userService.getUserById(6L)).thenReturn(user);
      when(groupHouseholdRepository.findByHouseholdIdAndGroupId(4L, 5L)).thenReturn(groupHousehold);

      groupHouseholdService.invite(request, token);
      
      verify(groupInviteService).createGroupInvite(2L, 5L);
    }
  }

  @Nested
  class AcceptInviteTests {

    @Test
    void shouldCreateNewGroupHouseholdRequestAndDeleteInvite() {
      Long userId = 2L;
      User user = new User();
      user.setId(userId);
      user.setHouseholdId(5L);

      Long groupId = 1L;
      EmergencyGroup group = new EmergencyGroup();
      group.setId(groupId);

      String token = "Bearer token";

      when(jwtService.extractUserId(token.substring(7))).thenReturn(2L);
      when(userService.getUserById(userId)).thenReturn(user);
      when(groupHouseholdRepository.findByHouseholdIdAndGroupId(5L, 1L)).thenReturn(null);
      when(groupInviteService.hasGroupInvite(5L, 1L)).thenReturn(true);

      GroupHouseholdRequest createRequest = new GroupHouseholdRequest();
      createRequest.setGroupId(groupId);
      createRequest.setHouseholdId(5L);

      GroupHousehold newGroupHousehold = new GroupHousehold();
      newGroupHousehold.setGroupId(groupId);
      newGroupHousehold.setHouseholdId(5L);

      try (MockedStatic<GroupHouseholdMapper> mapper 
          = Mockito.mockStatic(GroupHouseholdMapper.class)) {
        mapper.when(() -> GroupHouseholdMapper.toModel(createRequest))
            .thenReturn(newGroupHousehold);
        
        groupHouseholdService.acceptInvite(groupId, token);

        verify(groupHouseholdRepository).save(newGroupHousehold);
        verify(groupInviteService).deleteGroupInvite(5L, groupId);
      }
    }
  }

  @Nested
  class RefectInviteTests {


    @Test
    void shouldDeleteInvite() {
      Long userId = 1L;
      Long householdId = 3L;
      User user = new User();
      user.setId(userId);
      user.setHouseholdId(householdId);
      String token = "Bearer token";
      Long groupId = 10L;

      when(jwtService.extractUserId(token.substring(7))).thenReturn(userId);
      when(userService.getUserById(userId)).thenReturn(user);
      when(groupInviteService.hasGroupInvite(householdId, groupId)).thenReturn(true);

      groupHouseholdService.rejectInvite(groupId, token);

      verify(groupInviteService).deleteGroupInvite(householdId, groupId);
    }
  }
}
