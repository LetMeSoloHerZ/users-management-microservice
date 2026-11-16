package com.jcaa.usersmanagement.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.jcaa.usersmanagement.AplicacionTestData;
import com.jcaa.usersmanagement.application.port.out.GetAplicacionByNombrePort;
import com.jcaa.usersmanagement.application.port.out.SaveAplicacionPort;
import com.jcaa.usersmanagement.application.service.dto.command.CreateAplicacionCommand;
import com.jcaa.usersmanagement.domain.exception.AplicacionAlreadyExistsException;
import com.jcaa.usersmanagement.domain.exception.InvalidAplicacionException;
import com.jcaa.usersmanagement.domain.model.AplicacionModel;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("CreateAplicacionService")
@ExtendWith(MockitoExtension.class)
class CreateAplicacionServiceTest {

  @Mock private SaveAplicacionPort saveAplicacionPort;
  @Mock private GetAplicacionByNombrePort getAplicacionByNombrePort;
  @InjectMocks private CreateAplicacionService service;

  @Test
  @DisplayName("guarda la aplicacion con un id generado cuando el nombre esta libre")
  void shouldSaveWhenNombreIsFree() {
    when(getAplicacionByNombrePort.getByNombre(any())).thenReturn(Optional.empty());
    when(saveAplicacionPort.save(any(AplicacionModel.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    final AplicacionModel saved = service.execute(AplicacionTestData.createCommand());

    assertNotNull(saved.id());
    assertEquals("Visual Studio Code", saved.nombre().value());
    verify(saveAplicacionPort).save(any(AplicacionModel.class));
  }

  @Test
  @DisplayName("lanza AplicacionAlreadyExistsException si el nombre ya existe")
  void shouldFailWhenNombreExists() {
    when(getAplicacionByNombrePort.getByNombre(any()))
        .thenReturn(Optional.of(AplicacionTestData.model()));

    assertThrows(
        AplicacionAlreadyExistsException.class,
        () -> service.execute(AplicacionTestData.createCommand()));
    verify(saveAplicacionPort, never()).save(any());
  }

  @Test
  @DisplayName("lanza InvalidAplicacionException si la regla licencia/precio se incumple")
  void shouldFailWhenLicenseDoesNotMatchPrice() {
    final CreateAplicacionCommand base = AplicacionTestData.createCommand();
    final CreateAplicacionCommand invalid =
        new CreateAplicacionCommand(
            base.nombre(),
            base.proveedor(),
            base.categoria(),
            base.lenguajePrincipal(),
            base.lenguajeSecundario(),
            base.usaBd(),
            base.requiereConexionRed(),
            base.numBits(),
            base.sistemaOperativo(),
            base.requisitosHardware(),
            "GRATUITA",
            new BigDecimal("9.99"),
            base.descripcion(),
            base.web(),
            base.correo(),
            base.tamanoInstalador());

    assertThrows(InvalidAplicacionException.class, () -> service.execute(invalid));
    verify(saveAplicacionPort, never()).save(any());
  }
}
