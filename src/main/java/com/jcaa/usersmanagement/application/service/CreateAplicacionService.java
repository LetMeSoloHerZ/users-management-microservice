package com.jcaa.usersmanagement.application.service;

import com.jcaa.usersmanagement.application.port.in.CreateAplicacionUseCase;
import com.jcaa.usersmanagement.application.port.out.GetAplicacionByNombrePort;
import com.jcaa.usersmanagement.application.port.out.SaveAplicacionPort;
import com.jcaa.usersmanagement.application.service.dto.command.CreateAplicacionCommand;
import com.jcaa.usersmanagement.application.service.mapper.AplicacionApplicationMapper;
import com.jcaa.usersmanagement.domain.exception.AplicacionAlreadyExistsException;
import com.jcaa.usersmanagement.domain.model.AplicacionModel;
import com.jcaa.usersmanagement.domain.valueobject.AplicacionId;
import com.jcaa.usersmanagement.domain.valueobject.AplicacionNombre;
import org.springframework.stereotype.Service;

@Service
public class CreateAplicacionService implements CreateAplicacionUseCase {

  private final SaveAplicacionPort saveAplicacionPort;
  private final GetAplicacionByNombrePort getAplicacionByNombrePort;

  public CreateAplicacionService(
      final SaveAplicacionPort saveAplicacionPort,
      final GetAplicacionByNombrePort getAplicacionByNombrePort) {
    this.saveAplicacionPort = saveAplicacionPort;
    this.getAplicacionByNombrePort = getAplicacionByNombrePort;
  }

  @Override
  public AplicacionModel execute(final CreateAplicacionCommand command) {
    final AplicacionModel aplicacion =
        AplicacionApplicationMapper.fromCreateCommandToModel(command, AplicacionId.generate());
    ensureNombreIsNotTaken(aplicacion.nombre());
    return saveAplicacionPort.save(aplicacion);
  }

  private void ensureNombreIsNotTaken(final AplicacionNombre nombre) {
    getAplicacionByNombrePort
        .getByNombre(nombre)
        .ifPresent(
            ignored -> {
              throw AplicacionAlreadyExistsException.becauseNombreAlreadyExists(nombre.value());
            });
  }
}
