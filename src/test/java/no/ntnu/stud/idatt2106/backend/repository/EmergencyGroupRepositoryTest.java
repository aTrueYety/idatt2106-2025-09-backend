package no.ntnu.stud.idatt2106.backend.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import no.ntnu.stud.idatt2106.backend.model.base.EmergencyGroup;
import no.ntnu.stud.idatt2106.backend.model.base.GroupHousehold;
import no.ntnu.stud.idatt2106.backend.model.base.Household;
import no.ntnu.stud.idatt2106.backend.model.base.User;
import no.ntnu.stud.idatt2106.backend.model.response.EmergencyGroupSummaryResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

/**
 * Contains tests for the EmergencyGroupRepository class.
 */
@JdbcTest
@ActiveProfiles("test")
@Import({EmergencyGroupRepositoryImpl.class, HouseholdRepositoryImpl.class, 
  GroupHouseholdRepositoryImpl.class, UserRepositoryImpl.class})
public class EmergencyGroupRepositoryTest {
  
  @Autowired
  private EmergencyGroupRepository emergencyGroupRepository;

  @Autowired
  private HouseholdRepository householdRepository;

  @Autowired
  private GroupHouseholdRepository groupHouseholdRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @BeforeEach
  void setup() {
    //Reset DB and primary keys between tests.
    jdbcTemplate.execute("DELETE FROM emergency_group");
    jdbcTemplate.execute("ALTER TABLE emergency_group ALTER COLUMN id RESTART WITH 1");
  }

  @Test
  void shouldSaveEmergencyGroupAndFindById() {
    EmergencyGroup emergencyGroup = new EmergencyGroup();
    emergencyGroup.setName("Test Group");
    emergencyGroup.setDescription("Description");

    emergencyGroupRepository.save(emergencyGroup);
    Optional<EmergencyGroup> result = emergencyGroupRepository.findById(1L);

    assertTrue(result.isPresent());
    assertEquals(1L, result.get().getId());
    assertEquals("Test Group", result.get().getName());
    assertEquals("Description", result.get().getDescription());
  }

  @Test
  void shouldFindAllSavedEmergencyGroups() {
    EmergencyGroup emergencyGroup1 = new EmergencyGroup();
    emergencyGroup1.setName("Group1");
    emergencyGroup1.setDescription("Desc1");
    EmergencyGroup emergencyGroup2 = new EmergencyGroup();
    emergencyGroup2.setName("Group2");
    emergencyGroup2.setDescription("Desc2");

    emergencyGroupRepository.save(emergencyGroup1);
    emergencyGroupRepository.save(emergencyGroup2);

    List<EmergencyGroup> result = emergencyGroupRepository.findAll();

    assertEquals(2, result.size());
    
    EmergencyGroup result1 = result.get(0);

    assertEquals(1L, result1.getId());
    assertEquals("Group1", result1.getName());
    assertEquals("Desc1", result1.getDescription());

    EmergencyGroup result2 = result.get(1);
    assertEquals(2L, result2.getId());
    assertEquals("Group2", result2.getName());
    assertEquals("Desc2", result2.getDescription());
  }

  @Test
  void shouldDeleteAndReturnTrue() {
    EmergencyGroup emergencyGroup = new EmergencyGroup();
    emergencyGroup.setName("Group");

    emergencyGroupRepository.save(emergencyGroup);

    boolean result = emergencyGroupRepository.deleteById(1L);

    assertEquals(true, result);
    assertEquals(0, emergencyGroupRepository.findAll().size());
  }

  @Test
  void shouldUpdateExistingEmergencyGroup() {
    EmergencyGroup emergencyGroup = new EmergencyGroup();
    emergencyGroup.setName("Group");
    emergencyGroup.setDescription("Desc");

    EmergencyGroup updatedGroup = new EmergencyGroup();
    updatedGroup.setName("New Name");
    updatedGroup.setDescription("New desc");

    emergencyGroupRepository.save(emergencyGroup);
    emergencyGroupRepository.update(1L, updatedGroup);

    Optional<EmergencyGroup> result = emergencyGroupRepository.findById(1L);

    assertTrue(result.isPresent());
    assertEquals(1L, result.get().getId());
    assertEquals("New Name", result.get().getName());
    assertEquals("New desc", result.get().getDescription());
  }

  @Test
  void shouldFindGroupSummariesByHouseholdId() {
    EmergencyGroup emergencyGroup = new EmergencyGroup();
    emergencyGroup.setName("Group");
    emergencyGroup.setDescription("Desc");
    emergencyGroupRepository.save(emergencyGroup);

    Household household1 = new Household();
    household1.setName("Household1");
    household1.setLastWaterChangeDate(new Date());
    householdRepository.save(household1);
    Household household2 = new Household();
    household2.setName("Household2");
    household2.setLastWaterChangeDate(new Date());
    householdRepository.save(household2);

    GroupHousehold groupHousehold1 = new GroupHousehold();
    groupHousehold1.setGroupId(1L);
    groupHousehold1.setHouseholdId(1L);
    groupHouseholdRepository.save(groupHousehold1);
    GroupHousehold groupHousehold2  = new GroupHousehold();
    groupHousehold2.setGroupId(1L);
    groupHousehold2.setHouseholdId(2L);
    groupHouseholdRepository.save(groupHousehold2);

    User user1 = new User();
    user1.setUsername("user1");
    user1.setEmail("user1@email.com");
    user1.setHouseholdId(1L);
    user1.setPassword("password1");
    userRepository.addUser(user1);
    User user2 = new User();
    user2.setUsername("user2");
    user2.setEmail("use2@email.com");
    user2.setHouseholdId(2L);
    user2.setPassword("password2");
    userRepository.addUser(user2);


    List<EmergencyGroupSummaryResponse> result = emergencyGroupRepository
        .findGroupSummariesByHouseholdId(1L);

    assertEquals(1, result.size());
  }
}
