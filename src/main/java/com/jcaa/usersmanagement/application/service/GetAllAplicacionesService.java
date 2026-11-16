package com.jcaa.usersmanagement.application.service;

import com.jcaa.usersmanagement.application.port.in.GetAllAplicacionesUseCase;
import com.jcaa.usersmanagement.application.port.out.GetAllAplicacionesPort;
import com.jcaa.usersmanagement.domain.model.AplicacionModel;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class GetAllAplicacionesService implements GetAllAplicacionesUseCase {

  private final GetAllAplicacionesPort getAllAplicacionesPort;

  public GetAllAplicacionesService(final GetAllAplicacionesPort getAllAplicacionesPort) {
    this.getAllAplicacionesPort = getAllAplicacionesPort;
  }

  @Override
  public List<AplicacionModel> execute() {
    return getAllAplicacionesPort.getAll();
  }
}
