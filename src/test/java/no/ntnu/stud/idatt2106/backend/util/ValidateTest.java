package no.ntnu.stud.idatt2106.backend.util;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

/**
 * Test class for the Validate class.
 */
@ActiveProfiles("test")
@Import(Validate.class)
public class ValidateTest {
  @Test
  void testIsNull() {
    assert Validate.isNull().test(null);
    assert !Validate.isNull().test("not null");
  }

  @Test
  void testIsNotNull() {
    assert !Validate.isNotNull().test(null);
    assert Validate.isNotNull().test("not null");
  }

  @Test
  void testIsNotEmptyCollection() {
    assert !Validate.isNotEmptyCollection().test(null);
    assert !Validate.isNotEmptyCollection().test(List.of());
    assert Validate.isNotEmptyCollection().test(List.of("item"));
  }

  @Test
  void testIsNotEmpty() {
    assert !Validate.isNotEmpty().test(null);
    assert !Validate.isNotEmpty().test("");
    assert Validate.isNotEmpty().test("not empty");
  }

  @Test
  void testIsNotBlank() {
    assert !Validate.isNotBlank().test(null);
    assert !Validate.isNotBlank().test("");
    assert !Validate.isNotBlank().test("   ");
    assert Validate.isNotBlank().test("not blank");
  }

  @Test
  void testIsNotBlankOrNull() {
    assert !Validate.isNotBlankOrNull().test(null);
    assert !Validate.isNotBlankOrNull().test("");
    assert !Validate.isNotBlankOrNull().test("   ");
    assert Validate.isNotBlankOrNull().test("not blank or null");
  }

  @Test
  void testIsPositive() {
    assert !Validate.isPositive().test(null);
    assert !Validate.isPositive().test(-1);
    assert Validate.isPositive().test(1);
  }

  @Test
  void testIsNotPositive() {
    assert !Validate.isNotPositive().test(null);
    assert Validate.isNotPositive().test(-1);
    assert Validate.isNotPositive().test(0);
    assert !Validate.isNotPositive().test(1);
  }

  @Test
  void testIsZero() {
    assert !Validate.isZero().test(null);
    assert !Validate.isZero().test(-1);
    assert Validate.isZero().test(0);
    assert !Validate.isZero().test(1);
  }

  @Test
  void testIsNotZero() {
    assert !Validate.isNotZero().test(null);
    assert Validate.isNotZero().test(-1);
    assert !Validate.isNotZero().test(0);
    assert Validate.isNotZero().test(1);
  }

  @Test
  void testIsNegative() {
    assert !Validate.isNegative().test(null);
    assert Validate.isNegative().test(-1);
    assert !Validate.isNegative().test(0);
    assert !Validate.isNegative().test(1);
  }

  @Test
  void testIsNotNegative() {
    assert !Validate.isNotNegative().test(null);
    assert !Validate.isNotNegative().test(-1);
    assert Validate.isNotNegative().test(0);
    assert Validate.isNotNegative().test(1);
  }

  @Test
  void testIsTrue() {
    assert !Validate.isTrue().test(null);
    assert !Validate.isTrue().test(false);
    assert Validate.isTrue().test(true);
  }

  @Test
  void testIsFalse() {
    assert !Validate.isFalse().test(null);
    assert Validate.isFalse().test(false);
    assert !Validate.isFalse().test(true);
  }

  @Test
  void testThat() {
    assertDoesNotThrow(() -> Validate.that(1, Validate.isPositive(), "Value must be positive"));

    IllegalArgumentException exception1 = assertThrows(IllegalArgumentException.class,
        () -> Validate.that(-1, Validate.isPositive(), "Value must be positive"));
    assert exception1.getMessage().equals("Value must be positive");

    assertDoesNotThrow(() -> Validate.that("not empty", Validate.isNotEmpty()));

    IllegalArgumentException exception2 = assertThrows(IllegalArgumentException.class,
        () -> Validate.that(1, Validate.isZero()));
    assert exception2.getMessage().equals("Validation failed for value: 1");
  }

  @Test
  void testIsValidEmail() {
    assertDoesNotThrow(() -> Validate.isValid(true, "Valid input"));
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> Validate.isValid(false, "Invalid input"));
    assert exception.getMessage().equals("Invalid input");

    assertDoesNotThrow(() -> Validate.isValid(true));
    IllegalArgumentException exception2 = assertThrows(IllegalArgumentException.class,
        () -> Validate.isValid(false));
    assert exception2.getMessage().equals("Validation failed for value: false");
  }
}
