package com.jcaa.usersmanagement.infrastructure.adapter.persistence.repository;

import com.jcaa.usersmanagement.application.port.out.DeleteAplicacionPort;
import com.jcaa.usersmanagement.application.port.out.GetAllAplicacionesPort;
import com.jcaa.usersmanagement.application.port.out.GetAplicacionByIdPort;
import com.jcaa.usersmanagement.application.port.out.GetAplicacionByNombrePort;
import com.jcaa.usersmanagement.application.port.out.SaveAplicacionPort;
import com.jcaa.usersmanagement.application.port.out.UpdateAplicacionPort;
import com.jcaa.usersmanagement.domain.exception.AplicacionNotFoundException;
import com.jcaa.usersmanagement.domain.model.AplicacionModel;
import com.jcaa.usersmanagement.domain.valueobject.AplicacionId;
import com.jcaa.usersmanagement.domain.valueobject.AplicacionNombre;
import com.jcaa.usersmanagement.infrastructure.adapter.persistence.entity.AplicacionEntity;
import com.jcaa.usersmanagement.infrastructure.adapter.persistence.exception.AplicacionPersistenceException;
import com.jcaa.usersmanagement.infrastructure.adapter.persistence.mapper.AplicacionPersistenceMapper;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.stereotype.Repository;

/** Adaptador de persistencia (JDBC + MySQL) para la entidad Aplicacion. */
@Repository
public class AplicacionRepositoryMySQL
    implements SaveAplicacionPort,
        UpdateAplicacionPort,
        GetAplicacionByIdPort,
        GetAplicacionByNombrePort,
        GetAllAplicacionesPort,
        DeleteAplicacionPort {

  private static final String COLUMNS =
      "id, nombre, proveedor, categoria, lenguaje_principal, lenguaje_secundario, usa_bd, "
          + "requiere_conexion_red, num_bits, sistema_operativo, requisitos_hardware, licencia, "
          + "precio, descripcion, web, correo, tamano_instalador_mb";

  private static final String SQL_INSERT =
      "INSERT INTO aplicaciones ("
          + COLUMNS
          + ", created_at, updated_at) "
          + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW(), NOW())";

  private static final String SQL_UPDATE =
      "UPDATE aplicaciones SET nombre = ?, proveedor = ?, categoria = ?, lenguaje_principal = ?, "
          + "lenguaje_secundario = ?, usa_bd = ?, requiere_conexion_red = ?, num_bits = ?, "
          + "sistema_operativo = ?, requisitos_hardware = ?, licencia = ?, precio = ?, "
          + "descripcion = ?, web = ?, correo = ?, tamano_instalador_mb = ?, updated_at = NOW() "
          + "WHERE id = ?";

  private static final String SQL_SELECT_BY_ID =
      "SELECT " + COLUMNS + " FROM aplicaciones WHERE id = ? LIMIT 1";

  private static final String SQL_SELECT_BY_NOMBRE =
      "SELECT " + COLUMNS + " FROM aplicaciones WHERE nombre = ? LIMIT 1";

  private static final String SQL_SELECT_ALL =
      "SELECT " + COLUMNS + " FROM aplicaciones ORDER BY nombre ASC";

  private static final String SQL_DELETE = "DELETE FROM aplicaciones WHERE id = ?";

  private final DataSource dataSource;

  public AplicacionRepositoryMySQL(final DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public AplicacionModel save(final AplicacionModel aplicacion) {
    final AplicacionEntity entity = AplicacionPersistenceMapper.fromModelToEntity(aplicacion);
    try (final Connection connection = dataSource.getConnection();
        final PreparedStatement statement = connection.prepareStatement(SQL_INSERT)) {
      statement.setString(1, entity.id());
      bindEditableColumns(statement, entity, 2);
      statement.executeUpdate();
    } catch (final SQLException exception) {
      throw AplicacionPersistenceException.becauseSaveFailed(entity.id(), exception);
    }
    return findByIdOrFail(aplicacion.id());
  }

  @Override
  public AplicacionModel update(final AplicacionModel aplicacion) {
    final AplicacionEntity entity = AplicacionPersistenceMapper.fromModelToEntity(aplicacion);
    try (final Connection connection = dataSource.getConnection();
        final PreparedStatement statement = connection.prepareStatement(SQL_UPDATE)) {
      final int nextIndex = bindEditableColumns(statement, entity, 1);
      statement.setString(nextIndex, entity.id());
      statement.executeUpdate();
    } catch (final SQLException exception) {
      throw AplicacionPersistenceException.becauseUpdateFailed(entity.id(), exception);
    }
    return findByIdOrFail(aplicacion.id());
  }

  @Override
  public Optional<AplicacionModel> getById(final AplicacionId id) {
    try (final Connection connection = dataSource.getConnection();
        final PreparedStatement statement = connection.prepareStatement(SQL_SELECT_BY_ID)) {
      statement.setString(1, id.value());
      return readOne(statement);
    } catch (final SQLException exception) {
      throw AplicacionPersistenceException.becauseFindByIdFailed(id.value(), exception);
    }
  }

  @Override
  public Optional<AplicacionModel> getByNombre(final AplicacionNombre nombre) {
    try (final Connection connection = dataSource.getConnection();
        final PreparedStatement statement = connection.prepareStatement(SQL_SELECT_BY_NOMBRE)) {
      statement.setString(1, nombre.value());
      return readOne(statement);
    } catch (final SQLException exception) {
      throw AplicacionPersistenceException.becauseFindByNombreFailed(nombre.value(), exception);
    }
  }

  @Override
  public List<AplicacionModel> getAll() {
    try (final Connection connection = dataSource.getConnection();
        final PreparedStatement statement = connection.prepareStatement(SQL_SELECT_ALL);
        final ResultSet resultSet = statement.executeQuery()) {
      return AplicacionPersistenceMapper.fromResultSetToModelList(resultSet);
    } catch (final SQLException exception) {
      throw AplicacionPersistenceException.becauseFindAllFailed(exception);
    }
  }

  @Override
  public void delete(final AplicacionId id) {
    try (final Connection connection = dataSource.getConnection();
        final PreparedStatement statement = connection.prepareStatement(SQL_DELETE)) {
      statement.setString(1, id.value());
      statement.executeUpdate();
    } catch (final SQLException exception) {
      throw AplicacionPersistenceException.becauseDeleteFailed(id.value(), exception);
    }
  }

  private Optional<AplicacionModel> readOne(final PreparedStatement statement)
      throws SQLException {
    try (final ResultSet resultSet = statement.executeQuery()) {
      if (!resultSet.next()) {
        return Optional.empty();
      }
      return Optional.of(AplicacionPersistenceMapper.fromResultSetToModel(resultSet));
    }
  }

  /**
   * Asigna los 16 valores editables (todas las columnas menos el id) empezando en {@code
   * firstIndex}. Devuelve el siguiente indice libre.
   */
  private int bindEditableColumns(
      final PreparedStatement statement, final AplicacionEntity entity, final int firstIndex)
      throws SQLException {
    int index = firstIndex;
    statement.setString(index++, entity.nombre());
    statement.setString(index++, entity.proveedor());
    statement.setString(index++, entity.categoria());
    statement.setString(index++, entity.lenguajePrincipal());
    setNullableString(statement, index++, entity.lenguajeSecundario());
    statement.setBoolean(index++, entity.usaBd());
    statement.setBoolean(index++, entity.requiereConexionRed());
    statement.setInt(index++, entity.numBits());
    statement.setString(index++, entity.sistemaOperativo());
    setNullableString(statement, index++, entity.requisitosHardware());
    statement.setString(index++, entity.licencia());
    statement.setBigDecimal(index++, entity.precio());
    setNullableString(statement, index++, entity.descripcion());
    setNullableString(statement, index++, entity.web());
    statement.setString(index++, entity.correo());
    statement.setBigDecimal(index++, entity.tamanoInstaladorMb());
    return index;
  }

  private static void setNullableString(
      final PreparedStatement statement, final int index, final String value) throws SQLException {
    if (value == null) {
      statement.setNull(index, Types.VARCHAR);
    } else {
      statement.setString(index, value);
    }
  }

  private AplicacionModel findByIdOrFail(final AplicacionId id) {
    return getById(id)
        .orElseThrow(() -> AplicacionNotFoundException.becauseIdWasNotFound(id.value()));
  }
}
