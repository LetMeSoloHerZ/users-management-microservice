package com.jcaa.usersmanagement.domain.valueobject;

import com.jcaa.usersmanagement.domain.exception.InvalidAplicacionException;

public record AplicacionNombre(String value) {

  private static final String FIELD = "nombre";
  private static final int MINIMUM_LENGTH = 2;
  private static final int MAXIMUM_LENGTH = 100;

  public AplicacionNombre {
    final String normalizedValue = value == null ? "" : value.trim();
    if (normalizedValue.isEmpty()) {
      throw InvalidAplicacionException.becauseFieldIsEmpty(FIELD);
    }
    if (normalizedValue.length() < MINIMUM_LENGTH) {
      throw InvalidAplicacionException.becauseFieldIsTooShort(FIELD, MINIMUM_LENGTH);
    }
    if (normalizedValue.length() > MAXIMUM_LENGTH) {
      throw InvalidAplicacionException.becauseFieldIsTooLong(FIELD, MAXIMUM_LENGTH);
    }
    value = normalizedValue;
  }

  @Override
  public String toString() {
    return value;
  }
}
