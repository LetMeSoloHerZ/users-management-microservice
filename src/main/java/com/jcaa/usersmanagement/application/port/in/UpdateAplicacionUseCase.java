package com.jcaa.usersmanagement.application.port.in;

import com.jcaa.usersmanagement.application.service.dto.command.UpdateAplicacionCommand;
import com.jcaa.usersmanagement.domain.model.AplicacionModel;

public interface UpdateAplicacionUseCase {
  AplicacionModel execute(UpdateAplicacionCommand command);
}
