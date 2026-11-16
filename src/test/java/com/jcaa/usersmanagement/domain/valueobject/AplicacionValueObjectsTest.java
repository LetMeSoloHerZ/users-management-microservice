package com.jcaa.usersmanagement.domain.valueobject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.jcaa.usersmanagement.domain.exception.InvalidAplicacionException;
import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("Value objects de Aplicacion")
class AplicacionValueObjectsTest {

  // --- AplicacionId

  @Test
  @DisplayName("AplicacionId normaliza a minusculas y acepta UUID")
  void shouldAcceptUuid() {
    final AplicacionId id = new AplicacionId("  8F684DC4-0C67-4813-8D58-7B5E7F7937B8 ");
    assertEquals("8f684dc4-0c67-4813-8d58-7b5e7f7937b8", id.value());
  }

  @Test
  @DisplayName("AplicacionId.generate crea un UUID valido")
  void shouldGenerateValidId() {
    assertNotNull(new AplicacionId(AplicacionId.generate().value()));
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {"  ", "abc", "1234"})
  @DisplayName("AplicacionId rechaza vacio o formato invalido")
  void shouldRejectInvalidId(final String value) {
    assertThrows(InvalidAplicacionException.class, () -> new AplicacionId(value));
  }

  // --- AplicacionNombre

  @Test
  @DisplayName("AplicacionNombre recorta espacios")
  void shouldTrimNombre() {
    assertEquals("Moodle", new AplicacionNombre("  Moodle ").value());
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {" ", "a"})
  @DisplayName("AplicacionNombre rechaza vacio o menor de 2 caracteres")
  void shouldRejectShortNombre(final String value) {
    assertThrows(InvalidAplicacionException.class, () -> new AplicacionNombre(value));
  }

  @Test
  @DisplayName("AplicacionNombre rechaza mas de 100 caracteres")
  void shouldRejectLongNombre() {
    assertThrows(InvalidAplicacionException.class, () -> new AplicacionNombre("x".repeat(101)));
  }

  // --- AplicacionCorreo

  @Test
  @DisplayName("AplicacionCorreo normaliza a minusculas")
  void shouldLowercaseCorreo() {
    assertEquals("soporte@ejemplo.com", new AplicacionCorreo(" Soporte@Ejemplo.COM ").value());
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {"no-es-correo", "a@b", "@x.com"})
  @DisplayName("AplicacionCorreo rechaza vacio o formato invalido")
  void shouldRejectInvalidCorreo(final String value) {
    assertThrows(InvalidAplicacionException.class, () -> new AplicacionCorreo(value));
  }

  // --- AplicacionWeb

  @Test
  @DisplayName("AplicacionWeb acepta http y https")
  void shouldAcceptHttpUrls() {
    assertEquals("https://moodle.org", new AplicacionWeb("https://moodle.org").value());
    assertEquals("http://localhost:8080", new AplicacionWeb("http://localhost:8080").value());
  }

  @ParameterizedTest
  @ValueSource(strings = {"ftp://x.com", "moodle.org", "http://", "no es url"})
  @DisplayName("AplicacionWeb rechaza URLs que no son http/https")
  void shouldRejectInvalidWeb(final String value) {
    assertThrows(InvalidAplicacionException.class, () -> new AplicacionWeb(value));
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {"   "})
  @DisplayName("AplicacionWeb.fromNullable devuelve null cuando no hay valor")
  void shouldReturnNullWhenWebIsMissing(final String value) {
    assertNull(AplicacionWeb.fromNullable(value));
  }

  // --- Precio

  @Test
  @DisplayName("Precio redondea a 2 decimales y detecta el cero")
  void shouldRoundPrice() {
    assertEquals(new BigDecimal("22.99"), new Precio(new BigDecimal("22.991")).value());
    assertTrue(new Precio(BigDecimal.ZERO).isZero());
  }

  @Test
  @DisplayName("Precio rechaza nulo, negativo o demasiado grande")
  void shouldRejectInvalidPrice() {
    assertThrows(InvalidAplicacionException.class, () -> new Precio(null));
    assertThrows(InvalidAplicacionException.class, () -> new Precio(new BigDecimal("-1")));
    assertThrows(
        InvalidAplicacionException.class, () -> new Precio(new BigDecimal("100000000")));
  }

  // --- TamanoInstalador

  @Test
  @DisplayName("TamanoInstalador acepta valores positivos")
  void shouldAcceptPositiveSize() {
    assertEquals("95.50 MB", new TamanoInstalador(new BigDecimal("95.5")).toString());
  }

  @Test
  @DisplayName("TamanoInstalador rechaza nulo, cero o negativo")
  void shouldRejectInvalidSize() {
    assertThrows(InvalidAplicacionException.class, () -> new TamanoInstalador(null));
    assertThrows(InvalidAplicacionException.class, () -> new TamanoInstalador(BigDecimal.ZERO));
    assertThrows(
        InvalidAplicacionException.class, () -> new TamanoInstalador(new BigDecimal("-3")));
  }
}
