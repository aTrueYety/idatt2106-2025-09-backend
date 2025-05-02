package no.ntnu.stud.idatt2106.backend.util;

import java.util.Collection;
import java.util.function.Predicate;

/**
 * Utility class for validating objects.
 */
public class Validate {
  private Validate() {
    // Prevent instantiation
  }

  /**
   * Validates that a given value is null.
   *
   * @param <T> the type of the value to validate
   * @return a predicate that checks if the value is null
   */
  public static <T> Predicate<T> isNull() {
    return value -> value == null;
  }

  /**
   * Validates that a given value is not null.
   *
   * @param <T> the type of the value to validate
   * @return a predicate that checks if the value is not null
   */
  public static <T> Predicate<T> isNotNull() {
    return value -> value != null;
  }

  /**
   * Validates that a given value is an empty collection.
   *
   * @param <U> the type of the elements in the collection
   * @param <T> the type of the collection
   * @return a predicate that checks if the collection is empty
   */
  public static <U, T extends Collection<U>> Predicate<T> isNotEmptyCollection() {
    return value -> value != null && !value.isEmpty();
  }

  public static <U, T extends Collection<U>> Predicate<T> isEmptyCollection() {
    return value -> value == null || value.isEmpty();
  }

  public static Predicate<String> isNotEmpty() {
    return value -> value != null && !value.isEmpty();
  }

  public static Predicate<String> isNotBlank() {
    return value -> value != null && !value.isBlank();
  }

  public static Predicate<String> isNotBlankOrNull() {
    return value -> value != null && !value.isBlank();
  }

  public static Predicate<Number> isPositive() {
    return value -> value != null && value.doubleValue() > 0;
  }

  public static Predicate<Number> isNotPositive() {
    return value -> value != null && value.doubleValue() <= 0;
  }

  public static Predicate<Number> isZero() {
    return value -> value != null && value.doubleValue() == 0;
  }

  public static Predicate<Number> isNotZero() {
    return value -> value != null && value.doubleValue() != 0;
  }

  public static Predicate<Number> isNegative() {
    return value -> value != null && value.doubleValue() < 0;
  }

  public static Predicate<Number> isNotNegative() {
    return value -> value != null && value.doubleValue() >= 0;
  }

  public static Predicate<Boolean> isTrue() {
    return value -> value != null && value;
  }

  public static Predicate<Boolean> isFalse() {
    return value -> value != null && !value;
  }

  /**
   * Validates that a given value by the provided predicate and throws an
   * exception with the
   * provided message if validation fails.
   *
   * @param <T>       the type of the value to validate
   * @param value     the value to validate
   * @param predicate the predicate to validate the value against
   * @param message   the message to include in the exception if validation fails
   * @return the validated value
   */
  public static <T> T that(T value, Predicate<T> predicate, String message) {
    if (!predicate.test(value)) {
      throw new IllegalArgumentException(message);
    }
    return value;
  }

  /**
   * Validates that a given value by the provided predicate and throws an
   * exception with a default message if validation fails.
   *
   * @param <T>       the type of the value to validate
   * @param value     the value to validate
   * @param predicate the predicate to validate the value against
   * @return the validated value
   */
  public static <T> T that(T value, Predicate<T> predicate) {
    return that(value, predicate, "Validation failed for value: " + value);
  }

  /**
   * Validates that a given boolean value is true and throws an exception with the
   * provided message if validation fails.
   *
   * @param value the value to validate
   * @param message the message to include in the exception if validation fails
   * @return the validated value
   */
  public static boolean isValid(boolean value, String message) {
    if (!value) {
      throw new IllegalArgumentException(message);
    }
    return value;
  }

  /**
   * Validates that a given boolean value is true and throws an exception with a
   * default message if validation fails.
   *
   * @param value the value to validate
   * @return the validated value
   */
  public static boolean isValid(boolean value) {
    return isValid(value, "Validation failed for value: " + value);
  }
}
