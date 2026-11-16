package com.jcaa.usersmanagement.infrastructure.adapter.persistence.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.jcaa.usersmanagement.AplicacionTestData;
import com.jcaa.usersmanagement.domain.exception.AplicacionNotFoundException;
import com.jcaa.usersmanagement.domain.model.AplicacionModel;
import com.jcaa.usersmanagement.domain.valueobject.AplicacionId;
import com.jcaa.usersmanagement.domain.valueobject.AplicacionNombre;
import com.jcaa.usersmanagement.infrastructure.adapter.persistence.exception.AplicacionPersistenceException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("AplicacionRepositoryMySQL")
class AplicacionRepositoryMySQLTest {

  private static final String ID = AplicacionTestData.ID;

  private FakeJdbc jdbc;
  private AplicacionRepositoryMySQL repository;

  @BeforeEach
  void setUp() {
    jdbc = new FakeJdbc();
    repository = new AplicacionRepositoryMySQL(jdbc.dataSource());
  }

  @Test
  @DisplayName("save() inserta las 17 columnas y devuelve la fila leida de nuevo")
  void shouldInsertAndReturnSavedAplicacion() {
    jdbc.enqueueRows(List.of(FakeJdbc.row(ID, "Visual Studio Code")));

    final AplicacionModel saved = repository.save(AplicacionTestData.model());

    final FakeJdbc.Executed insert = jdbc.executed.get(0);
    assertTrue(insert.sql().startsWith("INSERT INTO aplicaciones"));
    assertEquals(17, insert.params().size());
    assertEquals(ID, insert.params().get(1));
    assertEquals("Visual Studio Code", insert.params().get(2));
    assertEquals("OPEN_SOURCE", insert.params().get(12));
    assertEquals(new BigDecimal("0.00"), insert.params().get(13));
    assertEquals(new BigDecimal("95.50"), insert.params().get(17));
    assertTrue(jdbc.executed.get(1).sql().startsWith("SELECT"));
    assertEquals("Visual Studio Code", saved.nombre().value());
  }

  @Test
  @DisplayName("save() convierte en NULL los campos opcionales vacios")
  void shouldBindNullForMissingOptionalFields() {
    final AplicacionModel sinOpcionales =
        new AplicacionModel(
            AplicacionTestData.model().id(),
            AplicacionTestData.model().nombre(),
            "Microsoft",
            AplicacionTestData.model().categoria(),
            "TypeScript",
            null,
            false,
            false,
            64,
            AplicacionTestData.model().sistemaOperativo(),
            null,
            AplicacionTestData.model().licencia(),
            AplicacionTestData.model().precio(),
            null,
            null,
            AplicacionTestData.model().correo(),
            AplicacionTestData.model().tamanoInstalador());
    jdbc.enqueueRows(List.of(FakeJdbc.row(ID, "Visual Studio Code")));

    repository.save(sinOpcionales);

    final FakeJdbc.Executed insert = jdbc.executed.get(0);
    assertNull(insert.params().get(6));
    assertNull(insert.params().get(11));
    assertNull(insert.params().get(14));
    assertNull(insert.params().get(15));
  }

  @Test
  @DisplayName("save() lanza AplicacionNotFoundException si no puede releer la fila insertada")
  void shouldFailWhenInsertedRowCannotBeRead() {
    assertThrows(
        AplicacionNotFoundException.class, () -> repository.save(AplicacionTestData.model()));
  }

  @Test
  @DisplayName("save() traduce SQLException a AplicacionPersistenceException")
  void shouldWrapSaveFailure() {
    jdbc.failWith(new SQLException("boom"));

    assertThrows(
        AplicacionPersistenceException.class, () -> repository.save(AplicacionTestData.model()));
  }

  @Test
  @DisplayName("update() actualiza y usa el id como ultimo parametro")
  void shouldUpdateAplicacion() {
    jdbc.enqueueRows(List.of(FakeJdbc.row(ID, "Visual Studio Code")));

    final AplicacionModel updated = repository.update(AplicacionTestData.model());

    final FakeJdbc.Executed update = jdbc.executed.get(0);
    assertTrue(update.sql().startsWith("UPDATE aplicaciones"));
    assertEquals(17, update.params().size());
    assertEquals("Visual Studio Code", update.params().get(1));
    assertEquals(ID, update.params().get(17));
    assertEquals(ID, updated.id().value());
  }

  @Test
  @DisplayName("update() traduce SQLException a AplicacionPersistenceException")
  void shouldWrapUpdateFailure() {
    jdbc.failWith(new SQLException("boom"));

    assertThrows(
        AplicacionPersistenceException.class, () -> repository.update(AplicacionTestData.model()));
  }

  @Test
  @DisplayName("getById() devuelve la aplicacion mapeada al dominio")
  void shouldGetById() {
    jdbc.enqueueRows(List.of(FakeJdbc.row(ID, "Visual Studio Code")));

    final Optional<AplicacionModel> result = repository.getById(new AplicacionId(ID));

    assertTrue(result.isPresent());
    assertEquals("MULTIPLATAFORMA", result.get().sistemaOperativo().name());
    assertEquals("https://code.visualstudio.com", result.get().web().value());
    assertTrue(result.get().requiereConexionRed());
    assertEquals(ID, jdbc.executed.get(0).params().get(1));
  }

  @Test
  @DisplayName("getById() devuelve vacio cuando no hay filas")
  void shouldReturnEmptyWhenIdDoesNotExist() {
    assertFalse(repository.getById(new AplicacionId(ID)).isPresent());
  }

  @Test
  @DisplayName("getById() traduce SQLException a AplicacionPersistenceException")
  void shouldWrapFindByIdFailure() {
    jdbc.failWith(new SQLException("boom"));
    final AplicacionId id = new AplicacionId(ID);

    assertThrows(AplicacionPersistenceException.class, () -> repository.getById(id));
  }

  @Test
  @DisplayName("getByNombre() busca por nombre y mapea el resultado")
  void shouldGetByNombre() {
    jdbc.enqueueRows(List.of(FakeJdbc.row(ID, "Visual Studio Code")));

    final Optional<AplicacionModel> result =
        repository.getByNombre(new AplicacionNombre("Visual Studio Code"));

    assertTrue(result.isPresent());
    assertEquals("Visual Studio Code", jdbc.executed.get(0).params().get(1));
  }

  @Test
  @DisplayName("getByNombre() devuelve vacio y traduce errores")
  void shouldHandleGetByNombreEmptyAndFailure() {
    final AplicacionNombre nombre = new AplicacionNombre("Nada");
    assertFalse(repository.getByNombre(nombre).isPresent());

    jdbc.failWith(new SQLException("boom"));
    assertThrows(AplicacionPersistenceException.class, () -> repository.getByNombre(nombre));
  }

  @Test
  @DisplayName("getAll() devuelve todas las filas mapeadas")
  void shouldGetAll() {
    jdbc.enqueueRows(
        List.of(
            FakeJdbc.row(ID, "Visual Studio Code"),
            FakeJdbc.row("11111111-1111-4111-8111-111111111111", "Zoom")));

    final List<AplicacionModel> result = repository.getAll();

    assertEquals(2, result.size());
    assertTrue(jdbc.executed.get(0).sql().endsWith("ORDER BY nombre ASC"));
  }

  @Test
  @DisplayName("getAll() traduce SQLException a AplicacionPersistenceException")
  void shouldWrapFindAllFailure() {
    jdbc.failWith(new SQLException("boom"));

    assertThrows(AplicacionPersistenceException.class, () -> repository.getAll());
  }

  @Test
  @DisplayName("delete() ejecuta DELETE con el id")
  void shouldDelete() {
    repository.delete(new AplicacionId(ID));

    assertTrue(jdbc.executed.get(0).sql().startsWith("DELETE FROM aplicaciones"));
    assertEquals(ID, jdbc.executed.get(0).params().get(1));
  }

  @Test
  @DisplayName("delete() traduce SQLException a AplicacionPersistenceException")
  void shouldWrapDeleteFailure() {
    jdbc.failWith(new SQLException("boom"));
    final AplicacionId id = new AplicacionId(ID);

    assertThrows(AplicacionPersistenceException.class, () -> repository.delete(id));
  }
}
