package com.jcaa.usersmanagement.infrastructure.adapter.persistence.mapper;

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
import com.jcaa.usersmanagement.infrastructure.adapter.persistence.entity.AplicacionEntity;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class AplicacionPersistenceMapper {

  private AplicacionPersistenceMapper() {}

  public static AplicacionEntity fromModelToEntity(final AplicacionModel model) {
    return new AplicacionEntity(
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

  public static AplicacionEntity fromResultSetToEntity(final ResultSet resultSet)
      throws SQLException {
    return new AplicacionEntity(
        resultSet.getString("id"),
        resultSet.getString("nombre"),
        resultSet.getString("proveedor"),
        resultSet.getString("categoria"),
        resultSet.getString("lenguaje_principal"),
        resultSet.getString("lenguaje_secundario"),
        resultSet.getBoolean("usa_bd"),
        resultSet.getBoolean("requiere_conexion_red"),
        resultSet.getInt("num_bits"),
        resultSet.getString("sistema_operativo"),
        resultSet.getString("requisitos_hardware"),
        resultSet.getString("licencia"),
        resultSet.getBigDecimal("precio"),
        resultSet.getString("descripcion"),
        resultSet.getString("web"),
        resultSet.getString("correo"),
        resultSet.getBigDecimal("tamano_instalador_mb"));
  }

  public static AplicacionModel fromEntityToModel(final AplicacionEntity entity) {
    return new AplicacionModel(
        new AplicacionId(entity.id()),
        new AplicacionNombre(entity.nombre()),
        entity.proveedor(),
        CategoriaAplicacion.fromString(entity.categoria()),
        entity.lenguajePrincipal(),
        entity.lenguajeSecundario(),
        entity.usaBd(),
        entity.requiereConexionRed(),
        entity.numBits(),
        SistemaOperativo.fromString(entity.sistemaOperativo()),
        entity.requisitosHardware(),
        Licencia.fromString(entity.licencia()),
        new Precio(entity.precio()),
        entity.descripcion(),
        AplicacionWeb.fromNullable(entity.web()),
        new AplicacionCorreo(entity.correo()),
        new TamanoInstalador(entity.tamanoInstaladorMb()));
  }

  public static AplicacionModel fromResultSetToModel(final ResultSet resultSet)
      throws SQLException {
    return fromEntityToModel(fromResultSetToEntity(resultSet));
  }

  public static List<AplicacionModel> fromResultSetToModelList(final ResultSet resultSet)
      throws SQLException {
    final List<AplicacionModel> aplicaciones = new ArrayList<>();
    while (resultSet.next()) {
      aplicaciones.add(fromResultSetToModel(resultSet));
    }
    return aplicaciones;
  }
}
