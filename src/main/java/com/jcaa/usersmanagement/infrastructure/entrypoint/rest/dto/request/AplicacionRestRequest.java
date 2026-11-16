package com.jcaa.usersmanagement.infrastructure.entrypoint.rest.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

/** Cuerpo JSON para crear (POST) o reemplazar (PUT) una aplicacion. */
public record AplicacionRestRequest(
    @Schema(example = "Visual Studio Code")
        @NotBlank(message = "nombre must not be blank")
        @Size(min = 2, max = 100, message = "nombre must have between 2 and 100 characters")
        String nombre,
    @Schema(example = "Microsoft")
        @NotBlank(message = "proveedor must not be blank")
        @Size(max = 100, message = "proveedor must not exceed 100 characters")
        String proveedor,
    @Schema(
            example = "DESARROLLO",
            description =
                "EDUCACION, PRODUCTIVIDAD, JUEGOS, UTILIDADES, DESARROLLO, COMUNICACION, "
                    + "MULTIMEDIA u OTRA")
        @NotBlank(message = "categoria must not be blank")
        String categoria,
    @Schema(example = "TypeScript")
        @NotBlank(message = "lenguajePrincipal must not be blank")
        @Size(max = 50, message = "lenguajePrincipal must not exceed 50 characters")
        String lenguajePrincipal,
    @Schema(example = "JavaScript", nullable = true)
        @Size(max = 50, message = "lenguajeSecundario must not exceed 50 characters")
        String lenguajeSecundario,
    @Schema(example = "false") @NotNull(message = "usaBd must not be null") Boolean usaBd,
    @Schema(example = "false")
        @NotNull(message = "requiereConexionRed must not be null")
        Boolean requiereConexionRed,
    @Schema(example = "64", description = "32 o 64")
        @NotNull(message = "numBits must not be null")
        Integer numBits,
    @Schema(
            example = "MULTIPLATAFORMA",
            description = "WINDOWS, LINUX, MACOS, ANDROID, IOS o MULTIPLATAFORMA")
        @NotBlank(message = "sistemaOperativo must not be blank")
        String sistemaOperativo,
    @Schema(example = "4 GB de RAM y 1 GB de disco", nullable = true)
        @Size(max = 500, message = "requisitosHardware must not exceed 500 characters")
        String requisitosHardware,
    @Schema(
            example = "OPEN_SOURCE",
            description = "GRATUITA, OPEN_SOURCE, COMERCIAL, SHAREWARE o FREEMIUM")
        @NotBlank(message = "licencia must not be blank")
        String licencia,
    @Schema(
            example = "0.00",
            description = "GRATUITA y OPEN_SOURCE exigen 0; COMERCIAL exige un valor mayor que 0")
        @NotNull(message = "precio must not be null")
        @DecimalMin(value = "0.00", message = "precio must be greater than or equal to 0")
        BigDecimal precio,
    @Schema(example = "Editor de codigo fuente ligero y extensible.", nullable = true)
        @Size(max = 1000, message = "descripcion must not exceed 1000 characters")
        String descripcion,
    @Schema(example = "https://code.visualstudio.com", nullable = true)
        @Size(max = 255, message = "web must not exceed 255 characters")
        String web,
    @Schema(example = "soporte@ejemplo.com")
        @NotBlank(message = "correo must not be blank")
        @Email(message = "correo must be a valid email address")
        @Size(max = 120, message = "correo must not exceed 120 characters")
        String correo,
    @Schema(example = "95.50", description = "Tamano del instalador en MB")
        @NotNull(message = "tamanoInstalador must not be null")
        @DecimalMin(
            value = "0.00",
            inclusive = false,
            message = "tamanoInstalador must be greater than 0")
        BigDecimal tamanoInstalador) {}
