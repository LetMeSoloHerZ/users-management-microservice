package com.jcaa.usersmanagement.domain.valueobject;

import com.jcaa.usersmanagement.domain.exception.InvalidAplicacionException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;

public record AplicacionWeb(String value) {

  private static final String FIELD = "web";
  private static final int MAXIMUM_LENGTH = 255;

  public AplicacionWeb {
    final String normalizedValue = value == null ? "" : value.trim();
    if (normalizedValue.isEmpty()) {
      throw InvalidAplicacionException.becauseFieldIsEmpty(FIELD);
    }
    if (normalizedValue.length() > MAXIMUM_LENGTH) {
      throw InvalidAplicacionException.becauseFieldIsTooLong(FIELD, MAXIMUM_LENGTH);
    }
    validateHttpUrl(normalizedValue);
    value = normalizedValue;
  }

  /** La web es opcional: devuelve {@code null} cuando el valor llega nulo o vacio. */
  public static AplicacionWeb fromNullable(final String value) {
    if (value == null || value.isBlank()) {
      return null;
    }
    return new AplicacionWeb(value);
  }

  private static void validateHttpUrl(final String candidate) {
    try {
      final URI uri = new URI(candidate);
      final String scheme = uri.getScheme() == null ? "" : uri.getScheme().toLowerCase(Locale.ROOT);
      final boolean httpScheme = "http".equals(scheme) || "https".equals(scheme);
      if (!httpScheme || uri.getHost() == null) {
        throw InvalidAplicacionException.becauseWebFormatIsInvalid(candidate);
      }
    } catch (final URISyntaxException exception) {
      throw InvalidAplicacionException.becauseWebFormatIsInvalid(candidate);
    }
  }

  @Override
  public String toString() {
    return value;
  }
}
