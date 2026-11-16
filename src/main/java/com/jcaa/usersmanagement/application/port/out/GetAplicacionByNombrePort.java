package com.jcaa.usersmanagement.application.port.out;

import com.jcaa.usersmanagement.domain.model.AplicacionModel;
import com.jcaa.usersmanagement.domain.valueobject.AplicacionNombre;
import java.util.Optional;

public interface GetAplicacionByNombrePort {
  Optional<AplicacionModel> getByNombre(AplicacionNombre nombre);
}
