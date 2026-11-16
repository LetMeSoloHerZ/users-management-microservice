package com.jcaa.usersmanagement.domain.enums;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.jcaa.usersmanagement.domain.exception.InvalidAplicacionException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Enums de Aplicacion")
class AplicacionEnumsTest {

  @Test
  @DisplayName("fromString ignora mayusculas y espacios")
  void shouldParseIgnoringCase() {
    assertEquals(CategoriaAplicacion.DESARROLLO, CategoriaAplicacion.fromString(" desarrollo "));
    assertEquals(SistemaOperativo.MACOS, SistemaOperativo.fromString("macos"));
    assertEquals(Licencia.OPEN_SOURCE, Licencia.fromString("Open_Source"));
  }

  @Test
  @DisplayName("fromString rechaza valores nulos o desconocidos")
  void shouldRejectUnknownValues() {
    assertThrows(InvalidAplicacionException.class, () -> CategoriaAplicacion.fromString("XYZ"));
    assertThrows(InvalidAplicacionException.class, () -> SistemaOperativo.fromString(null));
    assertThrows(InvalidAplicacionException.class, () -> Licencia.fromString(""));
  }

  @Test
  @DisplayName("Licencia expone las reglas de precio")
  void shouldExposePriceRules() {
    assertTrue(Licencia.GRATUITA.requiresZeroPrice());
    assertTrue(Licencia.OPEN_SOURCE.requiresZeroPrice());
    assertTrue(Licencia.COMERCIAL.requiresPositivePrice());
    assertFalse(Licencia.FREEMIUM.requiresZeroPrice());
    assertFalse(Licencia.SHAREWARE.requiresPositivePrice());
  }
}
