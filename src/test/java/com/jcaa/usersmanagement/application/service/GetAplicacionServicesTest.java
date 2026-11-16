package com.jcaa.usersmanagement.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.jcaa.usersmanagement.AplicacionTestData;
import com.jcaa.usersmanagement.application.port.out.GetAllAplicacionesPort;
import com.jcaa.usersmanagement.application.port.out.GetAplicacionByIdPort;
import com.jcaa.usersmanagement.application.service.dto.query.GetAplicacionByIdQuery;
import com.jcaa.usersmanagement.domain.exception.AplicacionNotFoundException;
import com.jcaa.usersmanagement.domain.model.AplicacionModel;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("GetAplicacion services")
@ExtendWith(MockitoExtension.class)
class GetAplicacionServicesTest {

  @Mock private GetAplicacionByIdPort getAplicacionByIdPort;
  @Mock private GetAllAplicacionesPort getAllAplicacionesPort;

  @Test
  @DisplayName("GetAplicacionByIdService devuelve la aplicacion encontrada")
  void shouldReturnAplicacionById() {
    final AplicacionModel model = AplicacionTestData.model();
    when(getAplicacionByIdPort.getById(any())).thenReturn(Optional.of(model));

    final AplicacionModel result =
        new GetAplicacionByIdService(getAplicacionByIdPort)
            .execute(new GetAplicacionByIdQuery(AplicacionTestData.ID));

    assertEquals(model, result);
  }

  @Test
  @DisplayName("GetAplicacionByIdService lanza AplicacionNotFoundException si no existe")
  void shouldFailWhenIdDoesNotExist() {
    when(getAplicacionByIdPort.getById(any())).thenReturn(Optional.empty());
    final GetAplicacionByIdService service = new GetAplicacionByIdService(getAplicacionByIdPort);
    final GetAplicacionByIdQuery query = new GetAplicacionByIdQuery(AplicacionTestData.ID);

    assertThrows(AplicacionNotFoundException.class, () -> service.execute(query));
  }

  @Test
  @DisplayName("GetAllAplicacionesService devuelve la lista del puerto")
  void shouldReturnAll() {
    when(getAllAplicacionesPort.getAll()).thenReturn(List.of(AplicacionTestData.model()));

    final List<AplicacionModel> result =
        new GetAllAplicacionesService(getAllAplicacionesPort).execute();

    assertEquals(1, result.size());
  }
}
