package com.jcaa.usersmanagement.domain.valueobject;

import com.jcaa.usersmanagement.domain.exception.InvalidAplicacionException;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Pattern;

public record AplicacionId(String value) {

  private static final Pattern UUID_PATTERN =
      Pattern.compile("^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$");

  public AplicacionId {
    final String normalizedValue = value == null ? "" : value.trim().toLowerCase(Locale.ROOT);
    if (normalizedValue.isEmpty()) {
      throw InvalidAplicacionException.becauseFieldIsEmpty("id");
    }
    if (!UUID_PATTERN.matcher(normalizedValue).matches()) {
      throw InvalidAplicacionException.becauseIdFormatIsInvalid(normalizedValue);
    }
    value = normalizedValue;
  }

  public static AplicacionId generate() {
    return new AplicacionId(UUID.randomUUID().toString());
  }

  @Override
  public String toString() {
    return value;
  }
}
