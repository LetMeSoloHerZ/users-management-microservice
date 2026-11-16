package com.jcaa.usersmanagement.domain.model;

import com.jcaa.usersmanagement.domain.enums.CategoriaAplicacion;
import com.jcaa.usersmanagement.domain.enums.Licencia;
import com.jcaa.usersmanagement.domain.enums.SistemaOperativo;
import com.jcaa.usersmanagement.domain.exception.InvalidAplicacionException;
import com.jcaa.usersmanagement.domain.valueobject.AplicacionCorreo;
import com.jcaa.usersmanagement.domain.valueobject.AplicacionId;
import com.jcaa.usersmanagement.domain.valueobject.AplicacionNombre;
import com.jcaa.usersmanagement.domain.valueobject.AplicacionWeb;
import com.jcaa.usersmanagement.domain.valueobject.Precio;
import com.jcaa.usersmanagement.domain.valueobject.TamanoInstalador;
import java.util.Objects;

/**
 * Modelo de dominio de una aplicacion de software (Ejercicio 21).
 *
 * <p>Es inmutable. Reglas: el segundo lenguaje debe diferir del principal, los bits son 32 o 64 y
 * la licencia condiciona el precio (GRATUITA y OPEN_SOURCE = 0; COMERCIAL &gt; 0).
 */
public record AplicacionModel(
    AplicacionId id,
    AplicacionNombre nombre,
    String proveedor,
    CategoriaAplicacion categoria,
    String lenguajePrincipal,
    String lenguajeSecundario,
    boolean usaBd,
    boolean requiereConexionRed,
    int numBits,
    SistemaOperativo sistemaOperativo,
    String requisitosHardware,
    Licencia licencia,
    Precio precio,
    String descripcion,
    AplicacionWeb web,
    AplicacionCorreo correo,
    TamanoInstalador tamanoInstalador) {

  private static final int BITS_32 = 32;
  private static final int BITS_64 = 64;

  public AplicacionModel {
    Objects.requireNonNull(id, "id cannot be null");
    Objects.requireNonNull(nombre, "nombre cannot be null");
    Objects.requireNonNull(categoria, "categoria cannot be null");
    Objects.requireNonNull(sistemaOperativo, "sistemaOperativo cannot be null");
    Objects.requireNonNull(licencia, "licencia cannot be null");
    Objects.requireNonNull(precio, "precio cannot be null");
    Objects.requireNonNull(correo, "correo cannot be null");
    Objects.requireNonNull(tamanoInstalador, "tamanoInstalador cannot be null");

    proveedor = requiredText(proveedor, "proveedor", 100);
    lenguajePrincipal = requiredText(lenguajePrincipal, "lenguajePrincipal", 50);
    lenguajeSecundario = optionalText(lenguajeSecundario, "lenguajeSecundario", 50);
    requisitosHardware = optionalText(requisitosHardware, "requisitosHardware", 500);
    descripcion = optionalText(descripcion, "descripcion", 1000);

    validateBits(numBits);
    validateLanguages(lenguajePrincipal, lenguajeSecundario);
    validateLicenseMatchesPrice(licencia, precio);
  }

  /** Registra una aplicacion nueva. */
  public static AplicacionModel create(
      final AplicacionId id,
      final AplicacionNombre nombre,
      final String proveedor,
      final CategoriaAplicacion categoria,
      final String lenguajePrincipal,
      final String lenguajeSecundario,
      final boolean usaBd,
      final boolean requiereConexionRed,
      final int numBits,
      final SistemaOperativo sistemaOperativo,
      final String requisitosHardware,
      final Licencia licencia,
      final Precio precio,
      final String descripcion,
      final AplicacionWeb web,
      final AplicacionCorreo correo,
      final TamanoInstalador tamanoInstalador) {
    return new AplicacionModel(
        id,
        nombre,
        proveedor,
        categoria,
        lenguajePrincipal,
        lenguajeSecundario,
        usaBd,
        requiereConexionRed,
        numBits,
        sistemaOperativo,
        requisitosHardware,
        licencia,
        precio,
        descripcion,
        web,
        correo,
        tamanoInstalador);
  }

  /** Indica si la aplicacion se distribuye sin costo (precio cero). */
  public boolean isFree() {
    return precio.isZero();
  }

  private static void validateBits(final int numBits) {
    if (numBits != BITS_32 && numBits != BITS_64) {
      throw InvalidAplicacionException.becauseBitsAreInvalid(numBits);
    }
  }

  private static void validateLanguages(final String principal, final String secundario) {
    if (secundario != null && secundario.equalsIgnoreCase(principal)) {
      throw InvalidAplicacionException.becauseSecondaryLanguageEqualsPrimary(principal);
    }
  }

  private static void validateLicenseMatchesPrice(final Licencia licencia, final Precio precio) {
    if (licencia.requiresZeroPrice() && !precio.isZero()) {
      throw InvalidAplicacionException.becauseLicenseRequiresZeroPrice(licencia.name());
    }
    if (licencia.requiresPositivePrice() && precio.isZero()) {
      throw InvalidAplicacionException.becauseLicenseRequiresPositivePrice(licencia.name());
    }
  }

  private static String requiredText(final String value, final String field, final int maxLength) {
    final String normalized = value == null ? "" : value.trim();
    if (normalized.isEmpty()) {
      throw InvalidAplicacionException.becauseFieldIsEmpty(field);
    }
    if (normalized.length() > maxLength) {
      throw InvalidAplicacionException.becauseFieldIsTooLong(field, maxLength);
    }
    return normalized;
  }

  private static String optionalText(final String value, final String field, final int maxLength) {
    if (value == null || value.isBlank()) {
      return null;
    }
    final String normalized = value.trim();
    if (normalized.length() > maxLength) {
      throw InvalidAplicacionException.becauseFieldIsTooLong(field, maxLength);
    }
    return normalized;
  }
}
