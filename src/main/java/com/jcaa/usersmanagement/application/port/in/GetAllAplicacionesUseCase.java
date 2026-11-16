package com.jcaa.usersmanagement.application.port.in;

import com.jcaa.usersmanagement.domain.model.AplicacionModel;
import java.util.List;

public interface GetAllAplicacionesUseCase {
  List<AplicacionModel> execute();
}
