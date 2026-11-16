package com.jcaa.usersmanagement.application.port.out;

import com.jcaa.usersmanagement.domain.model.AplicacionModel;
import com.jcaa.usersmanagement.domain.valueobject.AplicacionId;
import java.util.Optional;

public interface GetAplicacionByIdPort {
  Optional<AplicacionModel> getById(AplicacionId id);
}
