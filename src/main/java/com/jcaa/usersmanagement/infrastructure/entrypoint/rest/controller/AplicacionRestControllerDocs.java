package com.jcaa.usersmanagement.infrastructure.entrypoint.rest.controller;

import com.jcaa.usersmanagement.infrastructure.config.OpenApiConfig;
import com.jcaa.usersmanagement.infrastructure.entrypoint.rest.dto.request.AplicacionRestRequest;
import com.jcaa.usersmanagement.infrastructure.entrypoint.rest.dto.response.AplicacionRestResponse;
import com.jcaa.usersmanagement.infrastructure.entrypoint.rest.dto.response.ApiErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Contrato OpenAPI / Swagger para {@link AplicacionRestController}.
 *
 * <p>Toda la documentacion de la API vive aqui para mantener el controlador enfocado en despachar.
 */
@Tag(
    name = "Aplicaciones",
    description = "Gestion de aplicaciones de software: crear, consultar, actualizar y eliminar.")
public interface AplicacionRestControllerDocs {

  @SecurityRequirement(name = OpenApiConfig.BEARER_AUTH)
  @Operation(
      summary = "Crear aplicacion",
      description =
          "Registra una aplicacion nueva. El ID (UUID) lo genera el servidor y el nombre debe ser unico. Solo rol ADMIN.")
  @ApiResponses({
    @ApiResponse(
        responseCode = "201",
        description = "Aplicacion creada exitosamente.",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AplicacionRestResponse.class))),
    @ApiResponse(
        responseCode = "400",
        description = "Datos de entrada invalidos o regla de negocio incumplida (licencia/precio, bits, lenguajes).",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class))),
    @ApiResponse(
        responseCode = "401",
        description = "Falta el token JWT o es invalido.",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class))),
    @ApiResponse(
        responseCode = "403",
        description = "El rol del usuario no tiene permiso para esta operacion.",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class))),
    @ApiResponse(
        responseCode = "409",
        description = "Ya existe una aplicacion con ese nombre.",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class))),
    @ApiResponse(
        responseCode = "500",
        description = "Error interno del servidor.",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class)))
  })
  AplicacionRestResponse create(@Valid @RequestBody AplicacionRestRequest request);

  @SecurityRequirement(name = OpenApiConfig.BEARER_AUTH)
  @Operation(
      summary = "Listar aplicaciones",
      description =
          "Retorna todas las aplicaciones ordenadas alfabeticamente por nombre. Roles ADMIN y REVIEWER.")
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Lista obtenida exitosamente. Puede ser vacia.",
        content =
            @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = AplicacionRestResponse.class)))),
    @ApiResponse(
        responseCode = "401",
        description = "Falta el token JWT o es invalido.",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class))),
    @ApiResponse(
        responseCode = "403",
        description = "El rol del usuario no tiene permiso para esta operacion.",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class))),
    @ApiResponse(
        responseCode = "500",
        description = "Error interno del servidor.",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class)))
  })
  @SuppressWarnings("unused") // Invocado por Spring MVC y leido por springdoc mediante reflexion.
  List<AplicacionRestResponse> getAll();

  @SecurityRequirement(name = OpenApiConfig.BEARER_AUTH)
  @Operation(
      summary = "Obtener aplicacion por ID",
      description =
          "Retorna una aplicacion identificada por su UUID. Roles ADMIN y REVIEWER.")
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Aplicacion encontrada.",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AplicacionRestResponse.class))),
    @ApiResponse(
        responseCode = "400",
        description = "Datos de entrada invalidos o regla de negocio incumplida (licencia/precio, bits, lenguajes).",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class))),
    @ApiResponse(
        responseCode = "401",
        description = "Falta el token JWT o es invalido.",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class))),
    @ApiResponse(
        responseCode = "403",
        description = "El rol del usuario no tiene permiso para esta operacion.",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class))),
    @ApiResponse(
        responseCode = "404",
        description = "No existe ninguna aplicacion con el ID proporcionado.",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class))),
    @ApiResponse(
        responseCode = "500",
        description = "Error interno del servidor.",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class)))
  })
  AplicacionRestResponse getById(
      @Parameter(
              description = "UUID de la aplicacion.",
              example = "8f684dc4-0c67-4813-8d58-7b5e7f7937b8",
              required = true)
          @PathVariable
          String id);

  @SecurityRequirement(name = OpenApiConfig.BEARER_AUTH)
  @Operation(
      summary = "Actualizar aplicacion",
      description =
          "Reemplaza todos los datos de una aplicacion existente. Si cambia el nombre, no debe estar en uso por otra aplicacion. Solo rol ADMIN.")
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Aplicacion actualizada exitosamente.",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AplicacionRestResponse.class))),
    @ApiResponse(
        responseCode = "400",
        description = "Datos de entrada invalidos o regla de negocio incumplida (licencia/precio, bits, lenguajes).",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class))),
    @ApiResponse(
        responseCode = "401",
        description = "Falta el token JWT o es invalido.",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class))),
    @ApiResponse(
        responseCode = "403",
        description = "El rol del usuario no tiene permiso para esta operacion.",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class))),
    @ApiResponse(
        responseCode = "404",
        description = "No existe ninguna aplicacion con el ID proporcionado.",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class))),
    @ApiResponse(
        responseCode = "409",
        description = "Ya existe una aplicacion con ese nombre.",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class))),
    @ApiResponse(
        responseCode = "500",
        description = "Error interno del servidor.",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class)))
  })
  AplicacionRestResponse update(
      @Parameter(
              description = "UUID de la aplicacion a actualizar.",
              example = "8f684dc4-0c67-4813-8d58-7b5e7f7937b8",
              required = true)
          @PathVariable
          String id,
      @Valid @RequestBody AplicacionRestRequest request);

  @SecurityRequirement(name = OpenApiConfig.BEARER_AUTH)
  @Operation(
      summary = "Eliminar aplicacion",
      description =
          "Elimina permanentemente una aplicacion por su UUID. Solo rol ADMIN.")
  @ApiResponses({
    @ApiResponse(responseCode = "204", description = "Aplicacion eliminada exitosamente."),
    @ApiResponse(
        responseCode = "400",
        description = "Datos de entrada invalidos o regla de negocio incumplida (licencia/precio, bits, lenguajes).",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class))),
    @ApiResponse(
        responseCode = "401",
        description = "Falta el token JWT o es invalido.",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class))),
    @ApiResponse(
        responseCode = "403",
        description = "El rol del usuario no tiene permiso para esta operacion.",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class))),
    @ApiResponse(
        responseCode = "404",
        description = "No existe ninguna aplicacion con el ID proporcionado.",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class))),
    @ApiResponse(
        responseCode = "500",
        description = "Error interno del servidor.",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class)))
  })
  void delete(
      @Parameter(
              description = "UUID de la aplicacion a eliminar.",
              example = "8f684dc4-0c67-4813-8d58-7b5e7f7937b8",
              required = true)
          @PathVariable
          String id);
}
