package com.jcaa.usersmanagement.application.port.in;

import com.jcaa.usersmanagement.application.service.dto.query.GetAplicacionByIdQuery;
import com.jcaa.usersmanagement.domain.model.AplicacionModel;

public interface GetAplicacionByIdUseCase {
  AplicacionModel execute(GetAplicacionByIdQuery query);
}
