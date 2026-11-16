package com.jcaa.usersmanagement.application.port.in;

import com.jcaa.usersmanagement.application.service.dto.command.DeleteAplicacionCommand;

public interface DeleteAplicacionUseCase {
  void execute(DeleteAplicacionCommand command);
}
