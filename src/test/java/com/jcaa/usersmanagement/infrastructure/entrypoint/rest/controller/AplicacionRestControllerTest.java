package com.jcaa.usersmanagement.infrastructure.entrypoint.rest.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jcaa.usersmanagement.AplicacionTestData;
import com.jcaa.usersmanagement.application.port.in.CreateAplicacionUseCase;
import com.jcaa.usersmanagement.application.port.in.DeleteAplicacionUseCase;
import com.jcaa.usersmanagement.application.port.in.GetAllAplicacionesUseCase;
import com.jcaa.usersmanagement.application.port.in.GetAplicacionByIdUseCase;
import com.jcaa.usersmanagement.application.port.in.UpdateAplicacionUseCase;
import com.jcaa.usersmanagement.application.service.dto.command.DeleteAplicacionCommand;
import com.jcaa.usersmanagement.application.service.dto.query.GetAplicacionByIdQuery;
import com.jcaa.usersmanagement.domain.exception.AplicacionAlreadyExistsException;
import com.jcaa.usersmanagement.domain.exception.AplicacionNotFoundException;
import com.jcaa.usersmanagement.domain.exception.InvalidAplicacionException;
import com.jcaa.usersmanagement.domain.model.AplicacionModel;
import com.jcaa.usersmanagement.infrastructure.security.JwtAuthenticationFilter;
import com.jcaa.usersmanagement.infrastructure.security.JwtTokenService;
import com.jcaa.usersmanagement.infrastructure.security.RestAccessDeniedHandler;
import com.jcaa.usersmanagement.infrastructure.security.RestAuthenticationEntryPoint;
import com.jcaa.usersmanagement.infrastructure.security.SecurityConfig;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(
    controllers = AplicacionRestController.class,
    properties = "spring.main.web-application-type=servlet")
@Import({
  SecurityConfig.class,
  JwtAuthenticationFilter.class,
  RestAuthenticationEntryPoint.class,
  RestAccessDeniedHandler.class
})
@DisplayName("AplicacionRestController")
class AplicacionRestControllerTest {

  private static final String ID = AplicacionTestData.ID;
  private static final String VALID_BODY =
      """
      {
        "nombre": "Visual Studio Code",
        "proveedor": "Microsoft",
        "categoria": "DESARROLLO",
        "lenguajePrincipal": "TypeScript",
        "lenguajeSecundario": "JavaScript",
        "usaBd": false,
        "requiereConexionRed": false,
        "numBits": 64,
        "sistemaOperativo": "MULTIPLATAFORMA",
        "requisitosHardware": "4 GB de RAM",
        "licencia": "OPEN_SOURCE",
        "precio": 0,
        "descripcion": "Editor de codigo",
        "web": "https://code.visualstudio.com",
        "correo": "soporte@ejemplo.com",
        "tamanoInstalador": 95.50
      }
      """;

  @Autowired private MockMvc mockMvc;

  @MockBean private CreateAplicacionUseCase createAplicacionUseCase;
  @MockBean private UpdateAplicacionUseCase updateAplicacionUseCase;
  @MockBean private DeleteAplicacionUseCase deleteAplicacionUseCase;
  @MockBean private GetAplicacionByIdUseCase getAplicacionByIdUseCase;
  @MockBean private GetAllAplicacionesUseCase getAllAplicacionesUseCase;
  @MockBean private JwtTokenService jwtTokenService;

  @Test
  @WithMockUser(roles = "REVIEWER")
  @DisplayName("GET /api/aplicaciones devuelve la lista para un REVIEWER")
  void shouldListAplicaciones() throws Exception {
    when(getAllAplicacionesUseCase.execute()).thenReturn(List.of(AplicacionTestData.model()));

    mockMvc
        .perform(get("/api/aplicaciones"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value(ID))
        .andExpect(jsonPath("$[0].nombre").value("Visual Studio Code"))
        .andExpect(jsonPath("$[0].categoria").value("DESARROLLO"))
        .andExpect(jsonPath("$[0].licencia").value("OPEN_SOURCE"))
        .andExpect(jsonPath("$[0].numBits").value(64));
  }

  @Test
  @WithMockUser(roles = "REVIEWER")
  @DisplayName("GET /api/aplicaciones/{id} devuelve la aplicacion")
  void shouldGetAplicacionById() throws Exception {
    when(getAplicacionByIdUseCase.execute(new GetAplicacionByIdQuery(ID)))
        .thenReturn(AplicacionTestData.model());

    mockMvc
        .perform(get("/api/aplicaciones/{id}", ID))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.proveedor").value("Microsoft"))
        .andExpect(jsonPath("$.correo").value("soporte@ejemplo.com"));
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  @DisplayName("GET /api/aplicaciones/{id} responde 404 si no existe")
  void shouldReturnNotFound() throws Exception {
    when(getAplicacionByIdUseCase.execute(new GetAplicacionByIdQuery(ID)))
        .thenThrow(AplicacionNotFoundException.becauseIdWasNotFound(ID));

    mockMvc
        .perform(get("/api/aplicaciones/{id}", ID))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status").value(404));
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  @DisplayName("POST /api/aplicaciones crea una aplicacion y responde 201")
  void shouldCreateAplicacion() throws Exception {
    final AplicacionModel created = AplicacionTestData.model();
    when(createAplicacionUseCase.execute(ArgumentMatchers.any())).thenReturn(created);

    mockMvc
        .perform(
            post("/api/aplicaciones").contentType(MediaType.APPLICATION_JSON).content(VALID_BODY))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(ID))
        .andExpect(jsonPath("$.nombre").value("Visual Studio Code"));
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  @DisplayName("POST /api/aplicaciones responde 409 si el nombre ya existe")
  void shouldReturnConflictWhenNombreExists() throws Exception {
    when(createAplicacionUseCase.execute(ArgumentMatchers.any()))
        .thenThrow(AplicacionAlreadyExistsException.becauseNombreAlreadyExists("Visual Studio Code"));

    mockMvc
        .perform(
            post("/api/aplicaciones").contentType(MediaType.APPLICATION_JSON).content(VALID_BODY))
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.status").value(409));
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  @DisplayName("POST /api/aplicaciones responde 400 si una regla de dominio se incumple")
  void shouldReturnBadRequestOnDomainRuleViolation() throws Exception {
    when(createAplicacionUseCase.execute(ArgumentMatchers.any()))
        .thenThrow(InvalidAplicacionException.becauseLicenseRequiresZeroPrice("GRATUITA"));

    mockMvc
        .perform(
            post("/api/aplicaciones").contentType(MediaType.APPLICATION_JSON).content(VALID_BODY))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status").value(400));
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  @DisplayName("POST /api/aplicaciones rechaza un cuerpo invalido antes del caso de uso")
  void shouldRejectInvalidBody() throws Exception {
    final String invalid =
        """
        { "nombre": "", "proveedor": "", "categoria": "", "correo": "no-es-correo" }
        """;

    mockMvc
        .perform(post("/api/aplicaciones").contentType(MediaType.APPLICATION_JSON).content(invalid))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status").value(400));
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  @DisplayName("POST /api/aplicaciones responde 400 si el JSON esta mal formado")
  void shouldRejectMalformedJson() throws Exception {
    mockMvc
        .perform(post("/api/aplicaciones").contentType(MediaType.APPLICATION_JSON).content("{ no"))
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithMockUser(roles = "REVIEWER")
  @DisplayName("POST /api/aplicaciones rechaza a un REVIEWER con 403")
  void shouldForbidReviewerToCreate() throws Exception {
    mockMvc
        .perform(
            post("/api/aplicaciones").contentType(MediaType.APPLICATION_JSON).content(VALID_BODY))
        .andExpect(status().isForbidden())
        .andExpect(jsonPath("$.status").value(403));
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  @DisplayName("PUT /api/aplicaciones/{id} actualiza la aplicacion")
  void shouldUpdateAplicacion() throws Exception {
    when(updateAplicacionUseCase.execute(ArgumentMatchers.any()))
        .thenReturn(AplicacionTestData.model());

    mockMvc
        .perform(
            put("/api/aplicaciones/{id}", ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(VALID_BODY))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(ID));
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  @DisplayName("DELETE /api/aplicaciones/{id} responde 204")
  void shouldDeleteAplicacion() throws Exception {
    mockMvc.perform(delete("/api/aplicaciones/{id}", ID)).andExpect(status().isNoContent());

    verify(deleteAplicacionUseCase).execute(new DeleteAplicacionCommand(ID));
  }

  @Test
  @DisplayName("GET /api/aplicaciones exige autenticacion (401)")
  void shouldRequireAuthentication() throws Exception {
    mockMvc
        .perform(get("/api/aplicaciones"))
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("$.status").value(401));
  }

  @Test
  @WithMockUser(roles = "MEMBER")
  @DisplayName("GET /api/aplicaciones rechaza el rol MEMBER (403)")
  void shouldForbidMemberRole() throws Exception {
    mockMvc.perform(get("/api/aplicaciones")).andExpect(status().isForbidden());
  }
}
