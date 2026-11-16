package com.jcaa.usersmanagement.application.port.out;

import com.jcaa.usersmanagement.domain.model.AplicacionModel;
import java.util.List;

public interface GetAllAplicacionesPort {
  List<AplicacionModel> getAll();
}
