package com.jcaa.usersmanagement.application.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.jcaa.usersmanagement.AplicacionTestData;
import com.jcaa.usersmanagement.application.port.out.DeleteAplicacionPort;
import com.jcaa.usersmanagement.application.port.out.GetAplicacionByIdPort;
import com.jcaa.usersmanagement.application.service.dto.command.DeleteAplicacionCommand;
import com.jcaa.usersmanagement.domain.exception.AplicacionNotFoundException;
import com.jcaa.usersmanagement.domain.valueobject.AplicacionId;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("DeleteAplicacionService")
@ExtendWith(MockitoExtension.class)
class DeleteAplicacionServiceTest {

  @Mock private DeleteAplicacionPort deleteAplicacionPort;
  @Mock private GetAplicacionByIdPort getAplicacionByIdPort;
  @InjectMocks private DeleteAplicacionService service;

  @Test
  @DisplayName("elimina cuando la aplicacion existe")
  void shouldDeleteWhenExists() {
    when(getAplicacionByIdPort.getById(any())).thenReturn(Optional.of(AplicacionTestData.model()));

    service.execute(new DeleteAplicacionCommand(AplicacionTestData.ID));

    verify(deleteAplicacionPort).delete(new AplicacionId(AplicacionTestData.ID));
  }

  @Test
  @DisplayName("lanza AplicacionNotFoundException cuando no existe")
  void shouldFailWhenDoesNotExist() {
    when(getAplicacionByIdPort.getById(any())).thenReturn(Optional.empty());

    assertThrows(
        AplicacionNotFoundException.class,
        () -> service.execute(new DeleteAplicacionCommand(AplicacionTestData.ID)));
    verify(deleteAplicacionPort, never()).delete(any());
  }
}
