package com.jcaa.usersmanagement.infrastructure.entrypoint.rest.dto.response;

import java.math.BigDecimal;

public record AplicacionRestResponse(
    String id,
    String nombre,
    String proveedor,
    String categoria,
    String lenguajePrincipal,
    String lenguajeSecundario,
    boolean usaBd,
    boolean requiereConexionRed,
    int numBits,
    String sistemaOperativo,
    String requisitosHardware,
    String licencia,
    BigDecimal precio,
    String descripcion,
    String web,
    String correo,
    BigDecimal tamanoInstalador) {}
