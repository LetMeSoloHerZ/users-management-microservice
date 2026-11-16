package com.jcaa.usersmanagement.domain.enums;

import com.jcaa.usersmanagement.domain.exception.InvalidAplicacionException;
import java.util.Arrays;

public enum Licencia {
  GRATUITA,
  OPEN_SOURCE,
  COMERCIAL,
  SHAREWARE,
  FREEMIUM;

  private static final String FIELD = "licencia";

  public static Licencia fromString(final String value) {
    for (final Licencia licencia : values()) {
      if (licencia.name().equalsIgnoreCase(value == null ? "" : value.trim())) {
        return licencia;
      }
    }
    throw InvalidAplicacionException.becauseValueIsNotAllowed(
        FIELD, value, Arrays.toString(values()));
  }

  /** Las licencias gratuitas y de codigo abierto exigen precio cero. */
  public boolean requiresZeroPrice() {
    return this == GRATUITA || this == OPEN_SOURCE;
  }

  /** La licencia comercial exige un precio mayor que cero. */
  public boolean requiresPositivePrice() {
    return this == COMERCIAL;
  }
}
