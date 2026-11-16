package com.jcaa.usersmanagement.application.service.mapper;

import com.jcaa.usersmanagement.application.service.dto.command.CreateAplicacionCommand;
import com.jcaa.usersmanagement.application.service.dto.command.DeleteAplicacionCommand;
import com.jcaa.usersmanagement.application.service.dto.command.UpdateAplicacionCommand;
import com.jcaa.usersmanagement.application.service.dto.query.GetAplicacionByIdQuery;
import com.jcaa.usersmanagement.domain.enums.CategoriaAplicacion;
import com.jcaa.usersmanagement.domain.enums.Licencia;
import com.jcaa.usersmanagement.domain.enums.SistemaOperativo;
import com.jcaa.usersmanagement.domain.model.AplicacionModel;
import com.jcaa.usersmanagement.domain.valueobject.AplicacionCorreo;
import com.jcaa.usersmanagement.domain.valueobject.AplicacionId;
import com.jcaa.usersmanagement.domain.valueobject.AplicacionNombre;
import com.jcaa.usersmanagement.domain.valueobject.AplicacionWeb;
import com.jcaa.usersmanagement.domain.valueobject.Precio;
import com.jcaa.usersmanagement.domain.valueobject.TamanoInstalador;

public final class AplicacionApplicationMapper {

  private AplicacionApplicationMapper() {}

  public static AplicacionModel fromCreateCommandToModel(
      final CreateAplicacionCommand command, final AplicacionId id) {
    return AplicacionModel.create(
        id,
        new AplicacionNombre(command.nombre()),
        command.proveedor(),
        CategoriaAplicacion.fromString(command.categoria()),
        command.lenguajePrincipal(),
        command.lenguajeSecundario(),
        command.usaBd(),
        command.requiereConexionRed(),
        command.numBits(),
        SistemaOperativo.fromString(command.sistemaOperativo()),
        command.requisitosHardware(),
        Licencia.fromString(command.licencia()),
        new Precio(command.precio()),
        command.descripcion(),
        AplicacionWeb.fromNullable(command.web()),
        new AplicacionCorreo(command.correo()),
        new TamanoInstalador(command.tamanoInstalador()));
  }

  public static AplicacionModel fromUpdateCommandToModel(final UpdateAplicacionCommand command) {
    return new AplicacionModel(
        new AplicacionId(command.id()),
        new AplicacionNombre(command.nombre()),
        command.proveedor(),
        CategoriaAplicacion.fromString(command.categoria()),
        command.lenguajePrincipal(),
        command.lenguajeSecundario(),
        command.usaBd(),
        command.requiereConexionRed(),
        command.numBits(),
        SistemaOperativo.fromString(command.sistemaOperativo()),
        command.requisitosHardware(),
        Licencia.fromString(command.licencia()),
        new Precio(command.precio()),
        command.descripcion(),
        AplicacionWeb.fromNullable(command.web()),
        new AplicacionCorreo(command.correo()),
        new TamanoInstalador(command.tamanoInstalador()));
  }

  public static AplicacionId fromGetByIdQueryToId(final GetAplicacionByIdQuery query) {
    return new AplicacionId(query.id());
  }

  public static AplicacionId fromDeleteCommandToId(final DeleteAplicacionCommand command) {
    return new AplicacionId(command.id());
  }
}
