package no.ntnu.stud.idatt2106.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import no.ntnu.stud.idatt2106.backend.model.base.EmergencyGroup;
import no.ntnu.stud.idatt2106.backend.model.base.GroupHousehold;
import no.ntnu.stud.idatt2106.backend.model.base.User;
import no.ntnu.stud.idatt2106.backend.model.request.EmergencyGroupRequest;
import no.ntnu.stud.idatt2106.backend.model.response.EmergencyGroupResponse;
import no.ntnu.stud.idatt2106.backend.model.response.EmergencyGroupSummaryResponse;
import no.ntnu.stud.idatt2106.backend.repository.EmergencyGroupRepository;
import no.ntnu.stud.idatt2106.backend.repository.GroupHouseholdRepository;
import no.ntnu.stud.idatt2106.backend.service.mapper.EmergencyGroupMapper;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Contains unit tests for the EmergencyGroupService class.
 */
@ExtendWith(MockitoExtension.class)
public class EmergencyGroupServiceTest {
  
  @InjectMocks
  private EmergencyGroupService emergencyGroupService;

  @Mock
  private EmergencyGroupRepository repository;

  @Mock
  private UserService userService;

  @Mock
  private GroupHouseholdRepository groupHouseholdRepository;

  @Mock
  private GroupHouseholdService groupHouseholdService;

  @Mock
  private ExtraResidentService extraResidentService;

  @Mock
  private JwtService jwtService;

  @Nested
  class CreateTests {

    @Test
    void shouldConvertToModelAndSave() {
      EmergencyGroupRequest request = new EmergencyGroupRequest();
      request.setName("Group Name");
      when(jwtService.extractUserId(anyString())).thenReturn(1L);
      User user = new User();
      user.setHouseholdId(1L);
      when(userService.getUserById(1L)).thenReturn(user);
      EmergencyGroup group = new EmergencyGroup();
      when(repository.save(group)).thenReturn(group);

      try (MockedStatic<EmergencyGroupMapper> mapper = 
          Mockito.mockStatic(EmergencyGroupMapper.class)) {
        mapper.when(() -> EmergencyGroupMapper.toModel(request)).thenReturn(group);

        emergencyGroupService.create(request, "token123");

        verify(repository).save(group);
      }
    }
  }

  @Nested
  class GetAllTests {

    @Test
    void shouldMapToResponseAndReturnAll() {
      EmergencyGroup group1 = new EmergencyGroup();
      EmergencyGroup group2 = new EmergencyGroup();
      List<EmergencyGroup> groups = List.of(group1, group2);

      EmergencyGroupResponse response1 = new EmergencyGroupResponse();
      EmergencyGroupResponse response2 = new EmergencyGroupResponse();

      when(repository.findAll()).thenReturn(groups);

      try (MockedStatic<EmergencyGroupMapper> mapper 
          = Mockito.mockStatic(EmergencyGroupMapper.class)) {

        mapper.when(() -> EmergencyGroupMapper.toResponse(group1)).thenReturn(response1);
        mapper.when(() -> EmergencyGroupMapper.toResponse(group2)).thenReturn(response2);

        List<EmergencyGroupResponse> result = emergencyGroupService.getAll();

        assertEquals(List.of(response1, response2), result);
        verify(repository).findAll();
      }
    }
  }

  @Nested
  class DeleteTests {

    @Test
    void shouldDeleteAndReturnTrueIfGroupWithIdExists() {
      Long id = 2L;
      when(repository.deleteById(id)).thenReturn(true);

      User user = new User();
      Long userId = 1L;
      Long householdId = 2L;
      user.setId(userId);
      user.setHouseholdId(householdId);
      
      GroupHousehold groupHousehold = new GroupHousehold();
      groupHousehold.setHouseholdId(householdId);
      groupHousehold.setGroupId(id);

      when(groupHouseholdRepository.findByHouseholdIdAndGroupId(householdId, id))
            .thenReturn(groupHousehold);

      String token = "Bearer token";
      when(jwtService.extractUserId(token.substring(7))).thenReturn(userId);
      when(userService.getUserById(userId)).thenReturn(user);
      
      boolean response = emergencyGroupService.delete(id, token);

      assertTrue(response);
      verify(repository).deleteById(id);
    }

    @Test
    void shouldReturnFalseIfGroupWithIdDoesNotExist() {
      Long id = 1L;
      when(repository.deleteById(id)).thenReturn(false);

      User user = new User();
      Long userId = 1L;
      Long householdId = 2L;
      user.setId(userId);
      user.setHouseholdId(householdId);

      GroupHousehold groupHousehold = new GroupHousehold();
      groupHousehold.setHouseholdId(householdId);
      groupHousehold.setGroupId(id);

      when(groupHouseholdRepository.findByHouseholdIdAndGroupId(householdId, id))
            .thenReturn(groupHousehold);

      String token = "Bearer token";
      when(jwtService.extractUserId(token.substring(7))).thenReturn(userId);
      when(userService.getUserById(userId)).thenReturn(user);
      
      boolean response = emergencyGroupService.delete(id, token);

      assertFalse(response);
      verify(repository).deleteById(id);
    }
  }

  @Nested
  class UpdateTests {

    @Test
    void shouldUpdateEmergencyGroupWithId() {
      Long groupId = 2L;
      EmergencyGroup existingGroup = new EmergencyGroup();
      existingGroup.setId(groupId);
      existingGroup.setName("Name");
      existingGroup.setDescription("Description");

      String updatedName = "New name";
      String updatedDescription = "New description";
      EmergencyGroupRequest request = new EmergencyGroupRequest();
      request.setName(updatedName);
      request.setDescription(updatedDescription);

      EmergencyGroup updatedGroup = new EmergencyGroup();
      updatedGroup.setId(groupId);
      updatedGroup.setName(updatedName);
      updatedGroup.setDescription(updatedDescription);

      User user = new User();
      Long userId = 1L;
      Long householdId = 2L;
      user.setId(userId);
      user.setHouseholdId(householdId);

      GroupHousehold groupHousehold = new GroupHousehold();
      groupHousehold.setHouseholdId(householdId);
      groupHousehold.setGroupId(groupId);

      when(groupHouseholdRepository.findByHouseholdIdAndGroupId(householdId, groupId))
          .thenReturn(groupHousehold);

      when(repository.update(groupId, updatedGroup)).thenReturn(true);

      String token = "Bearer token";
      when(jwtService.extractUserId(token.substring(7))).thenReturn(userId);
      when(userService.getUserById(userId)).thenReturn(user);
    
      boolean result = emergencyGroupService.update(groupId, request, token);
      
      assertTrue(result);
      verify(repository).update(groupId, updatedGroup);
    }
  }

  @Nested
  class GetByIdTests {

    @Test
    void shouldReturnResponseIfGroupExists() {
      Long groupId = 1L;
      EmergencyGroup group = new EmergencyGroup();

      EmergencyGroupResponse expected = new EmergencyGroupResponse();

      try (MockedStatic<EmergencyGroupMapper> mapper 
          = Mockito.mockStatic(EmergencyGroupMapper.class)) {

        when(repository.findById(groupId)).thenReturn(Optional.of(group));
        mapper.when(() -> EmergencyGroupMapper.toResponse(group)).thenReturn(expected);

        EmergencyGroupResponse response = emergencyGroupService.getById(groupId);

        assertEquals(expected, response);
        verify(repository).findById(groupId);
      }
    }

    @Test
    void shouldThrowIfGroupDoesNotExist() {
      Long groupId = 1L;

      when(repository.findById(groupId)).thenReturn(Optional.empty());

      Exception exception = assertThrows(NoSuchElementException.class, () -> {
        emergencyGroupService.getById(groupId);
      });

      assertTrue(exception.getMessage().contains("Emergency group with ID = 1 not found"));
    }
  }

  @Nested
  class GetGroupSummariesByHouseholdIdTests {

    @Test
    void shouldReturnEmergencyGroupSummaryResponses() {
      EmergencyGroupSummaryResponse response1 = new EmergencyGroupSummaryResponse();
      EmergencyGroupSummaryResponse response2 = new EmergencyGroupSummaryResponse();
      List<EmergencyGroupSummaryResponse> responses = List.of(response1, response2);

      Long householdId = 2L;
      when(repository.findGroupSummariesByHouseholdId(householdId)).thenReturn(responses);

      List<EmergencyGroupSummaryResponse> result = 
          emergencyGroupService.getGroupSummariesByHouseholdId(householdId);

      assertEquals(result, responses);
      verify(repository).findGroupSummariesByHouseholdId(householdId);
    }
  }
}
