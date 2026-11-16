package com.jcaa.usersmanagement.infrastructure.adapter.persistence.entity;

import java.math.BigDecimal;

/** Fila de la tabla {@code aplicaciones}, con los tipos tal como los maneja JDBC. */
public record AplicacionEntity(
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
    BigDecimal tamanoInstaladorMb) {}
