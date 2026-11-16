package com.jcaa.usersmanagement.infrastructure.entrypoint.rest.mapper;

import com.jcaa.usersmanagement.application.service.dto.command.CreateAplicacionCommand;
import com.jcaa.usersmanagement.application.service.dto.command.DeleteAplicacionCommand;
import com.jcaa.usersmanagement.application.service.dto.command.UpdateAplicacionCommand;
import com.jcaa.usersmanagement.application.service.dto.query.GetAplicacionByIdQuery;
import com.jcaa.usersmanagement.domain.model.AplicacionModel;
import com.jcaa.usersmanagement.infrastructure.entrypoint.rest.dto.request.AplicacionRestRequest;
import com.jcaa.usersmanagement.infrastructure.entrypoint.rest.dto.response.AplicacionRestResponse;
import java.util.List;

public final class AplicacionRestMapper {

  private AplicacionRestMapper() {}

  public static CreateAplicacionCommand toCreateCommand(final AplicacionRestRequest request) {
    return new CreateAplicacionCommand(
        request.nombre(),
        request.proveedor(),
        request.categoria(),
        request.lenguajePrincipal(),
        request.lenguajeSecundario(),
        request.usaBd(),
        request.requiereConexionRed(),
        request.numBits(),
        request.sistemaOperativo(),
        request.requisitosHardware(),
        request.licencia(),
        request.precio(),
        request.descripcion(),
        request.web(),
        request.correo(),
        request.tamanoInstalador());
  }

  public static UpdateAplicacionCommand toUpdateCommand(
      final String id, final AplicacionRestRequest request) {
    return new UpdateAplicacionCommand(
        id,
        request.nombre(),
        request.proveedor(),
        request.categoria(),
        request.lenguajePrincipal(),
        request.lenguajeSecundario(),
        request.usaBd(),
        request.requiereConexionRed(),
        request.numBits(),
        request.sistemaOperativo(),
        request.requisitosHardware(),
        request.licencia(),
        request.precio(),
        request.descripcion(),
        request.web(),
        request.correo(),
        request.tamanoInstalador());
  }

  public static GetAplicacionByIdQuery toGetByIdQuery(final String id) {
    return new GetAplicacionByIdQuery(id);
  }

  public static DeleteAplicacionCommand toDeleteCommand(final String id) {
    return new DeleteAplicacionCommand(id);
  }

  public static AplicacionRestResponse toResponse(final AplicacionModel model) {
    return new AplicacionRestResponse(
        model.id().value(),
        model.nombre().value(),
        model.proveedor(),
        model.categoria().name(),
        model.lenguajePrincipal(),
        model.lenguajeSecundario(),
        model.usaBd(),
        model.requiereConexionRed(),
        model.numBits(),
        model.sistemaOperativo().name(),
        model.requisitosHardware(),
        model.licencia().name(),
        model.precio().value(),
        model.descripcion(),
        model.web() == null ? null : model.web().value(),
        model.correo().value(),
        model.tamanoInstalador().value());
  }

  public static List<AplicacionRestResponse> toResponseList(final List<AplicacionModel> models) {
    return models.stream().map(AplicacionRestMapper::toResponse).toList();
  }
}
