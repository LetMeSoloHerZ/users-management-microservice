package com.jcaa.usersmanagement.infrastructure.entrypoint.rest.controller;

import com.jcaa.usersmanagement.application.port.in.CreateAplicacionUseCase;
import com.jcaa.usersmanagement.application.port.in.DeleteAplicacionUseCase;
import com.jcaa.usersmanagement.application.port.in.GetAllAplicacionesUseCase;
import com.jcaa.usersmanagement.application.port.in.GetAplicacionByIdUseCase;
import com.jcaa.usersmanagement.application.port.in.UpdateAplicacionUseCase;
import com.jcaa.usersmanagement.domain.model.AplicacionModel;
import com.jcaa.usersmanagement.infrastructure.entrypoint.rest.dto.request.AplicacionRestRequest;
import com.jcaa.usersmanagement.infrastructure.entrypoint.rest.dto.response.AplicacionRestResponse;
import com.jcaa.usersmanagement.infrastructure.entrypoint.rest.mapper.AplicacionRestMapper;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/aplicaciones")
public class AplicacionRestController implements AplicacionRestControllerDocs {

  private final CreateAplicacionUseCase createAplicacionUseCase;
  private final UpdateAplicacionUseCase updateAplicacionUseCase;
  private final DeleteAplicacionUseCase deleteAplicacionUseCase;
  private final GetAplicacionByIdUseCase getAplicacionByIdUseCase;
  private final GetAllAplicacionesUseCase getAllAplicacionesUseCase;

  public AplicacionRestController(
      final CreateAplicacionUseCase createAplicacionUseCase,
      final UpdateAplicacionUseCase updateAplicacionUseCase,
      final DeleteAplicacionUseCase deleteAplicacionUseCase,
      final GetAplicacionByIdUseCase getAplicacionByIdUseCase,
      final GetAllAplicacionesUseCase getAllAplicacionesUseCase) {
    this.createAplicacionUseCase = createAplicacionUseCase;
    this.updateAplicacionUseCase = updateAplicacionUseCase;
    this.deleteAplicacionUseCase = deleteAplicacionUseCase;
    this.getAplicacionByIdUseCase = getAplicacionByIdUseCase;
    this.getAllAplicacionesUseCase = getAllAplicacionesUseCase;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public AplicacionRestResponse create(@Valid @RequestBody final AplicacionRestRequest request) {
    final AplicacionModel aplicacion =
        createAplicacionUseCase.execute(AplicacionRestMapper.toCreateCommand(request));
    return AplicacionRestMapper.toResponse(aplicacion);
  }

  @GetMapping
  public List<AplicacionRestResponse> getAll() {
    return AplicacionRestMapper.toResponseList(getAllAplicacionesUseCase.execute());
  }

  @GetMapping("/{id}")
  public AplicacionRestResponse getById(@PathVariable final String id) {
    final AplicacionModel aplicacion =
        getAplicacionByIdUseCase.execute(AplicacionRestMapper.toGetByIdQuery(id));
    return AplicacionRestMapper.toResponse(aplicacion);
  }

  @PutMapping("/{id}")
  public AplicacionRestResponse update(
      @PathVariable final String id, @Valid @RequestBody final AplicacionRestRequest request) {
    final AplicacionModel aplicacion =
        updateAplicacionUseCase.execute(AplicacionRestMapper.toUpdateCommand(id, request));
    return AplicacionRestMapper.toResponse(aplicacion);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable final String id) {
    deleteAplicacionUseCase.execute(AplicacionRestMapper.toDeleteCommand(id));
  }
}
