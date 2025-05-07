package no.ntnu.stud.idatt2106.backend.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyFloat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import no.ntnu.stud.idatt2106.backend.model.base.SharedFood;
import no.ntnu.stud.idatt2106.backend.model.base.SharedFoodKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

/**
 * Tests for SharedFoodRepository.
 */
@ExtendWith(MockitoExtension.class)
class SharedFoodRepositoryTest {

  @Mock
  private JdbcTemplate jdbcTemplate;

  @InjectMocks
  private SharedFoodRepositoryImpl repository;

  private SharedFoodKey key;
  private SharedFood sample;

  @BeforeEach
  void setUp() {
    key = new SharedFoodKey(1L, 2L);
    sample = new SharedFood(key, 3.5f);
  }

  @Test
  void save_shouldCallJdbcTemplateUpdate_withCorrectSqlAndParameters() {
    repository.save(sample);

    verify(jdbcTemplate).update(
        eq("INSERT INTO shared_food (food_id, group_household_id, amount) VALUES (?, ?, ?)"),
        eq(key.getFoodId()),
        eq(key.getGroupHouseholdId()),
        eq(sample.getAmount()));
  }

  @Test
  void findById_whenFound_shouldReturnOptionalOfSharedFood() {
    List<SharedFood> returned = Collections.singletonList(sample);
    when(jdbcTemplate.query(
        eq("SELECT * FROM shared_food WHERE food_id = ? AND group_household_id = ?"),
        ArgumentMatchers.<RowMapper<SharedFood>>any(),
        eq(key.getFoodId()),
        eq(key.getGroupHouseholdId()))).thenReturn(returned);

    Optional<SharedFood> result = repository.findById(key);
    assertTrue(result.isPresent());
    assertEquals(sample, result.get());
  }

  @Test
  void findById_whenNotFound_shouldReturnEmptyOptional() {
    when(jdbcTemplate.query(
        anyString(), 
        ArgumentMatchers.<RowMapper<SharedFood>>any(), 
        any(), 
        any())).thenReturn(Collections.emptyList());

    Optional<SharedFood> result = repository.findById(key);
    assertFalse(result.isPresent());
  }

  @Test
  void findAll_shouldReturnListOfSharedFood() {
    List<SharedFood> list = Arrays.asList(
        new SharedFood(new SharedFoodKey(1L, 1L), 1.0f),
        new SharedFood(new SharedFoodKey(2L, 2L), 2.0f));
    when(jdbcTemplate.query(
        eq("SELECT * FROM shared_food"), 
        ArgumentMatchers.<RowMapper<SharedFood>>any())).thenReturn(list);

    List<SharedFood> result = repository.findAll();
    assertEquals(list, result);
  }

  @Test
  void update_whenRowUpdated_shouldReturnTrue() {
    when(jdbcTemplate.update(
        eq("UPDATE shared_food SET amount = ? WHERE food_id = ? AND group_household_id = ?"),
        eq(sample.getAmount()),
        eq(key.getFoodId()),
        eq(key.getGroupHouseholdId()))).thenReturn(1);

    assertTrue(repository.update(sample));
  }

  @Test
  void update_whenNoRowsUpdated_shouldReturnFalse() {
    when(jdbcTemplate.update(anyString(), anyFloat(), anyLong(), anyLong())).thenReturn(0);

    assertFalse(repository.update(sample));
  }

  @Test
  void deleteById_whenRowDeleted_shouldReturnTrue() {
    when(jdbcTemplate.update(
        eq("DELETE FROM shared_food WHERE food_id = ? AND group_household_id = ?"),
        eq(key.getFoodId()),
        eq(key.getGroupHouseholdId()))).thenReturn(1);

    assertTrue(repository.deleteById(key));
  }

  @Test
  void deleteById_whenNoRowDeleted_shouldReturnFalse() {
    when(jdbcTemplate.update(anyString(), anyLong(), anyLong())).thenReturn(0);

    assertFalse(repository.deleteById(key));
  }

  @Test
  void findByGroupHouseholdId_shouldReturnListOfSharedFood() {
    Long groupId = 2L;
    List<SharedFood> list = Arrays.asList(sample);
    when(jdbcTemplate.query(
        eq("SELECT * FROM shared_food WHERE group_household_id = ?"),
        ArgumentMatchers.<RowMapper<SharedFood>>any(),
        eq(groupId))).thenReturn(list);

    List<SharedFood> result = repository.findByGroupHouseholdId(groupId);
    assertEquals(list, result);
  }
}
