package com.jcaa.usersmanagement.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.jcaa.usersmanagement.AplicacionTestData;
import com.jcaa.usersmanagement.application.port.out.GetAplicacionByIdPort;
import com.jcaa.usersmanagement.application.port.out.GetAplicacionByNombrePort;
import com.jcaa.usersmanagement.application.port.out.UpdateAplicacionPort;
import com.jcaa.usersmanagement.domain.enums.Licencia;
import com.jcaa.usersmanagement.domain.exception.AplicacionAlreadyExistsException;
import com.jcaa.usersmanagement.domain.exception.AplicacionNotFoundException;
import com.jcaa.usersmanagement.domain.model.AplicacionModel;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("UpdateAplicacionService")
@ExtendWith(MockitoExtension.class)
class UpdateAplicacionServiceTest {

  @Mock private UpdateAplicacionPort updateAplicacionPort;
  @Mock private GetAplicacionByIdPort getAplicacionByIdPort;
  @Mock private GetAplicacionByNombrePort getAplicacionByNombrePort;
  @InjectMocks private UpdateAplicacionService service;

  @Test
  @DisplayName("actualiza cuando existe y el nombre pertenece a la misma aplicacion")
  void shouldUpdateWhenAplicacionExists() {
    final AplicacionModel existing = AplicacionTestData.model();
    when(getAplicacionByIdPort.getById(any())).thenReturn(Optional.of(existing));
    when(getAplicacionByNombrePort.getByNombre(any())).thenReturn(Optional.of(existing));
    when(updateAplicacionPort.update(any(AplicacionModel.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    final AplicacionModel updated = service.execute(AplicacionTestData.updateCommand());

    assertEquals("8 GB de RAM", updated.requisitosHardware());
    verify(updateAplicacionPort).update(any(AplicacionModel.class));
  }

  @Test
  @DisplayName("lanza AplicacionNotFoundException si no existe")
  void shouldFailWhenAplicacionDoesNotExist() {
    when(getAplicacionByIdPort.getById(any())).thenReturn(Optional.empty());

    assertThrows(
        AplicacionNotFoundException.class, () -> service.execute(AplicacionTestData.updateCommand()));
    verify(updateAplicacionPort, never()).update(any());
  }

  @Test
  @DisplayName("lanza AplicacionAlreadyExistsException si el nombre es de otra aplicacion")
  void shouldFailWhenNombreBelongsToAnotherAplicacion() {
    final AplicacionModel existing = AplicacionTestData.model();
    final AplicacionModel other =
        new AplicacionModel(
            new com.jcaa.usersmanagement.domain.valueobject.AplicacionId(
                "11111111-1111-4111-8111-111111111111"),
            existing.nombre(),
            existing.proveedor(),
            existing.categoria(),
            existing.lenguajePrincipal(),
            existing.lenguajeSecundario(),
            existing.usaBd(),
            existing.requiereConexionRed(),
            existing.numBits(),
            existing.sistemaOperativo(),
            existing.requisitosHardware(),
            Licencia.OPEN_SOURCE,
            existing.precio(),
            existing.descripcion(),
            existing.web(),
            existing.correo(),
            existing.tamanoInstalador());
    when(getAplicacionByIdPort.getById(any())).thenReturn(Optional.of(existing));
    when(getAplicacionByNombrePort.getByNombre(any())).thenReturn(Optional.of(other));

    assertThrows(
        AplicacionAlreadyExistsException.class,
        () -> service.execute(AplicacionTestData.updateCommand()));
    verify(updateAplicacionPort, never()).update(any());
  }
}
