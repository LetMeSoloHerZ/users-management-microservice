package com.jcaa.usersmanagement.application.port.out;

import com.jcaa.usersmanagement.domain.valueobject.AplicacionId;

public interface DeleteAplicacionPort {
  void delete(AplicacionId id);
}
