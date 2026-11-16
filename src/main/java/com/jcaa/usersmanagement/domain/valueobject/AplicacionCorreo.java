package com.jcaa.usersmanagement.domain.valueobject;

import com.jcaa.usersmanagement.domain.exception.InvalidAplicacionException;
import java.util.Locale;
import java.util.regex.Pattern;

public record AplicacionCorreo(String value) {

  private static final String FIELD = "correo";
  private static final int MAXIMUM_LENGTH = 120;
  private static final Pattern EMAIL_PATTERN =
      Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)+$");

  public AplicacionCorreo {
    final String normalizedValue = value == null ? "" : value.trim();
    if (normalizedValue.isEmpty()) {
      throw InvalidAplicacionException.becauseFieldIsEmpty(FIELD);
    }
    if (normalizedValue.length() > MAXIMUM_LENGTH) {
      throw InvalidAplicacionException.becauseFieldIsTooLong(FIELD, MAXIMUM_LENGTH);
    }
    if (!EMAIL_PATTERN.matcher(normalizedValue).matches()) {
      throw InvalidAplicacionException.becauseEmailFormatIsInvalid(normalizedValue);
    }
    value = normalizedValue.toLowerCase(Locale.ROOT);
  }

  @Override
  public String toString() {
    return value;
  }
}
