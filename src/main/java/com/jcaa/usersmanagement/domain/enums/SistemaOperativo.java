package com.jcaa.usersmanagement.domain.enums;

import com.jcaa.usersmanagement.domain.exception.InvalidAplicacionException;
import java.util.Arrays;

public enum SistemaOperativo {
  WINDOWS,
  LINUX,
  MACOS,
  ANDROID,
  IOS,
  MULTIPLATAFORMA;

  private static final String FIELD = "sistemaOperativo";

  public static SistemaOperativo fromString(final String value) {
    for (final SistemaOperativo sistema : values()) {
      if (sistema.name().equalsIgnoreCase(value == null ? "" : value.trim())) {
        return sistema;
      }
    }
    throw InvalidAplicacionException.becauseValueIsNotAllowed(
        FIELD, value, Arrays.toString(values()));
  }
}
