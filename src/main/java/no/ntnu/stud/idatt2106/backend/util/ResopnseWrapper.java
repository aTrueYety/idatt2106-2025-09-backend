package no.ntnu.stud.idatt2106.backend.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A generic response wrapper class used to standardize API responses.
 * It contains the data and a message.
 *
 * @param <T> the type of the data being wrapped
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResopnseWrapper<T> {
  private String message;
  private T data;
}
