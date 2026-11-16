package com.jcaa.usersmanagement.application.service;

import com.jcaa.usersmanagement.application.port.in.DeleteAplicacionUseCase;
import com.jcaa.usersmanagement.application.port.out.DeleteAplicacionPort;
import com.jcaa.usersmanagement.application.port.out.GetAplicacionByIdPort;
import com.jcaa.usersmanagement.application.service.dto.command.DeleteAplicacionCommand;
import com.jcaa.usersmanagement.application.service.mapper.AplicacionApplicationMapper;
import com.jcaa.usersmanagement.domain.exception.AplicacionNotFoundException;
import com.jcaa.usersmanagement.domain.valueobject.AplicacionId;
import org.springframework.stereotype.Service;

@Service
public class DeleteAplicacionService implements DeleteAplicacionUseCase {

  private final DeleteAplicacionPort deleteAplicacionPort;
  private final GetAplicacionByIdPort getAplicacionByIdPort;

  public DeleteAplicacionService(
      final DeleteAplicacionPort deleteAplicacionPort,
      final GetAplicacionByIdPort getAplicacionByIdPort) {
    this.deleteAplicacionPort = deleteAplicacionPort;
    this.getAplicacionByIdPort = getAplicacionByIdPort;
  }

  @Override
  public void execute(final DeleteAplicacionCommand command) {
    final AplicacionId id = AplicacionApplicationMapper.fromDeleteCommandToId(command);
    getAplicacionByIdPort
        .getById(id)
        .orElseThrow(() -> AplicacionNotFoundException.becauseIdWasNotFound(id.value()));
    deleteAplicacionPort.delete(id);
  }
}
