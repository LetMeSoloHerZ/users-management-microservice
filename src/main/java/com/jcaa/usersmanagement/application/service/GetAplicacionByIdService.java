package com.jcaa.usersmanagement.application.service;

import com.jcaa.usersmanagement.application.port.in.GetAplicacionByIdUseCase;
import com.jcaa.usersmanagement.application.port.out.GetAplicacionByIdPort;
import com.jcaa.usersmanagement.application.service.dto.query.GetAplicacionByIdQuery;
import com.jcaa.usersmanagement.application.service.mapper.AplicacionApplicationMapper;
import com.jcaa.usersmanagement.domain.exception.AplicacionNotFoundException;
import com.jcaa.usersmanagement.domain.model.AplicacionModel;
import com.jcaa.usersmanagement.domain.valueobject.AplicacionId;
import org.springframework.stereotype.Service;

@Service
public class GetAplicacionByIdService implements GetAplicacionByIdUseCase {

  private final GetAplicacionByIdPort getAplicacionByIdPort;

  public GetAplicacionByIdService(final GetAplicacionByIdPort getAplicacionByIdPort) {
    this.getAplicacionByIdPort = getAplicacionByIdPort;
  }

  @Override
  public AplicacionModel execute(final GetAplicacionByIdQuery query) {
    final AplicacionId id = AplicacionApplicationMapper.fromGetByIdQueryToId(query);
    return getAplicacionByIdPort
        .getById(id)
        .orElseThrow(() -> AplicacionNotFoundException.becauseIdWasNotFound(id.value()));
  }
}
