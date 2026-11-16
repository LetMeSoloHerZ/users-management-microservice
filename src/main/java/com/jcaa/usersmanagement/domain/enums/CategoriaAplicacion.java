package com.jcaa.usersmanagement.domain.enums;

import com.jcaa.usersmanagement.domain.exception.InvalidAplicacionException;
import java.util.Arrays;

public enum CategoriaAplicacion {
  EDUCACION,
  PRODUCTIVIDAD,
  JUEGOS,
  UTILIDADES,
  DESARROLLO,
  COMUNICACION,
  MULTIMEDIA,
  OTRA;

  private static final String FIELD = "categoria";

  public static CategoriaAplicacion fromString(final String value) {
    for (final CategoriaAplicacion categoria : values()) {
      if (categoria.name().equalsIgnoreCase(value == null ? "" : value.trim())) {
        return categoria;
      }
    }
    throw InvalidAplicacionException.becauseValueIsNotAllowed(
        FIELD, value, Arrays.toString(values()));
  }
}
