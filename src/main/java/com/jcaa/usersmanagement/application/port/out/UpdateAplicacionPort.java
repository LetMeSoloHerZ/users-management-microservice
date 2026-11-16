package com.jcaa.usersmanagement.application.port.out;

import com.jcaa.usersmanagement.domain.model.AplicacionModel;

public interface UpdateAplicacionPort {
  AplicacionModel update(AplicacionModel aplicacion);
}
