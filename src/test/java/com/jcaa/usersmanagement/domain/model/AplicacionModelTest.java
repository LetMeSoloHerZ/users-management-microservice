package com.jcaa.usersmanagement.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.jcaa.usersmanagement.AplicacionTestData;
import com.jcaa.usersmanagement.domain.enums.CategoriaAplicacion;
import com.jcaa.usersmanagement.domain.enums.Licencia;
import com.jcaa.usersmanagement.domain.enums.SistemaOperativo;
import com.jcaa.usersmanagement.domain.exception.InvalidAplicacionException;
import com.jcaa.usersmanagement.domain.valueobject.AplicacionCorreo;
import com.jcaa.usersmanagement.domain.valueobject.AplicacionId;
import com.jcaa.usersmanagement.domain.valueobject.AplicacionNombre;
import com.jcaa.usersmanagement.domain.valueobject.Precio;
import com.jcaa.usersmanagement.domain.valueobject.TamanoInstalador;
import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("AplicacionModel")
class AplicacionModelTest {

  private static AplicacionModel build(
      final String proveedor,
      final String principal,
      final String secundario,
      final int bits,
      final Licencia licencia,
      final String precio) {
    return AplicacionModel.create(
        new AplicacionId(AplicacionTestData.ID),
        new AplicacionNombre("App Demo"),
        proveedor,
        CategoriaAplicacion.OTRA,
        principal,
        secundario,
        true,
        true,
        bits,
        SistemaOperativo.LINUX,
        null,
        licencia,
        new Precio(new BigDecimal(precio)),
        null,
        null,
        new AplicacionCorreo("demo@ejemplo.com"),
        new TamanoInstalador(new BigDecimal("10")));
  }

  @Test
  @DisplayName("crea una aplicacion valida y normaliza los textos")
  void shouldCreateValidAplicacion() {
    final AplicacionModel model = build("  Acme  ", " Java ", "  ", 32, Licencia.FREEMIUM, "5");

    assertEquals("Acme", model.proveedor());
    assertEquals("Java", model.lenguajePrincipal());
    assertNull(model.lenguajeSecundario());
    assertNull(model.web());
    assertFalse(model.isFree());
  }

  @Test
  @DisplayName("una licencia gratuita con precio cero es gratis")
  void shouldBeFreeWhenPriceIsZero() {
    assertTrue(AplicacionTestData.model().isFree());
  }

  @Test
  @DisplayName("rechaza un proveedor vacio")
  void shouldRejectBlankProvider() {
    assertThrows(
        InvalidAplicacionException.class,
        () -> build("   ", "Java", null, 64, Licencia.FREEMIUM, "1"));
  }

  @Test
  @DisplayName("rechaza un proveedor de mas de 100 caracteres")
  void shouldRejectTooLongProvider() {
    assertThrows(
        InvalidAplicacionException.class,
        () -> build("x".repeat(101), "Java", null, 64, Licencia.FREEMIUM, "1"));
  }

  @Test
  @DisplayName("rechaza bits distintos de 32 o 64")
  void shouldRejectInvalidBits() {
    assertThrows(
        InvalidAplicacionException.class,
        () -> build("Acme", "Java", null, 16, Licencia.FREEMIUM, "1"));
  }

  @Test
  @DisplayName("rechaza un lenguaje secundario igual al principal (sin distinguir mayusculas)")
  void shouldRejectSameSecondaryLanguage() {
    assertThrows(
        InvalidAplicacionException.class,
        () -> build("Acme", "Java", "JAVA", 64, Licencia.FREEMIUM, "1"));
  }

  @Test
  @DisplayName("las licencias gratuita y open source exigen precio cero")
  void shouldRequireZeroPriceForFreeLicenses() {
    assertThrows(
        InvalidAplicacionException.class,
        () -> build("Acme", "Java", null, 64, Licencia.GRATUITA, "1"));
    assertThrows(
        InvalidAplicacionException.class,
        () -> build("Acme", "Java", null, 64, Licencia.OPEN_SOURCE, "0.5"));
  }

  @Test
  @DisplayName("la licencia comercial exige precio mayor que cero")
  void shouldRequirePositivePriceForCommercialLicense() {
    assertThrows(
        InvalidAplicacionException.class,
        () -> build("Acme", "Java", null, 64, Licencia.COMERCIAL, "0"));
    assertEquals(
        new BigDecimal("22.99"),
        build("Acme", "Java", null, 64, Licencia.COMERCIAL, "22.99").precio().value());
  }
}
