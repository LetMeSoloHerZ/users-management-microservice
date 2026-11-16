package com.jcaa.usersmanagement.application.service;

import com.jcaa.usersmanagement.application.port.in.UpdateAplicacionUseCase;
import com.jcaa.usersmanagement.application.port.out.GetAplicacionByIdPort;
import com.jcaa.usersmanagement.application.port.out.GetAplicacionByNombrePort;
import com.jcaa.usersmanagement.application.port.out.UpdateAplicacionPort;
import com.jcaa.usersmanagement.application.service.dto.command.UpdateAplicacionCommand;
import com.jcaa.usersmanagement.application.service.mapper.AplicacionApplicationMapper;
import com.jcaa.usersmanagement.domain.exception.AplicacionAlreadyExistsException;
import com.jcaa.usersmanagement.domain.exception.AplicacionNotFoundException;
import com.jcaa.usersmanagement.domain.model.AplicacionModel;
import org.springframework.stereotype.Service;

@Service
public class UpdateAplicacionService implements UpdateAplicacionUseCase {

  private final UpdateAplicacionPort updateAplicacionPort;
  private final GetAplicacionByIdPort getAplicacionByIdPort;
  private final GetAplicacionByNombrePort getAplicacionByNombrePort;

  public UpdateAplicacionService(
      final UpdateAplicacionPort updateAplicacionPort,
      final GetAplicacionByIdPort getAplicacionByIdPort,
      final GetAplicacionByNombrePort getAplicacionByNombrePort) {
    this.updateAplicacionPort = updateAplicacionPort;
    this.getAplicacionByIdPort = getAplicacionByIdPort;
    this.getAplicacionByNombrePort = getAplicacionByNombrePort;
  }

  @Override
  public AplicacionModel execute(final UpdateAplicacionCommand command) {
    final AplicacionModel aplicacion =
        AplicacionApplicationMapper.fromUpdateCommandToModel(command);
    ensureAplicacionExists(aplicacion);
    ensureNombreBelongsToNobodyElse(aplicacion);
    return updateAplicacionPort.update(aplicacion);
  }

  private void ensureAplicacionExists(final AplicacionModel aplicacion) {
    getAplicacionByIdPort
        .getById(aplicacion.id())
        .orElseThrow(
            () -> AplicacionNotFoundException.becauseIdWasNotFound(aplicacion.id().value()));
  }

  private void ensureNombreBelongsToNobodyElse(final AplicacionModel aplicacion) {
    getAplicacionByNombrePort
        .getByNombre(aplicacion.nombre())
        .filter(other -> !other.id().equals(aplicacion.id()))
        .ifPresent(
            ignored -> {
              throw AplicacionAlreadyExistsException.becauseNombreAlreadyExists(
                  aplicacion.nombre().value());
            });
  }
}
