package com.jcaa.usersmanagement.infrastructure.adapter.persistence.repository;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;

/**
 * Doble de prueba de JDBC construido solo con {@link Proxy}: registra el SQL ejecutado y los
 * parametros asignados, y entrega filas predefinidas en cada {@code executeQuery()}.
 */
final class FakeJdbc {

  /** Una sentencia ejecutada: su SQL y los parametros asignados por indice. */
  record Executed(String sql, Map<Integer, Object> params) {}

  final List<Executed> executed = new ArrayList<>();
  private final Deque<List<Map<String, Object>>> queryResults = new ArrayDeque<>();
  private SQLException failure;

  /** Encola las filas que devolvera el siguiente {@code executeQuery()}. */
  void enqueueRows(final List<Map<String, Object>> rows) {
    queryResults.add(rows);
  }

  /** Hace que {@code getConnection()} falle con la excepcion indicada. */
  void failWith(final SQLException exception) {
    this.failure = exception;
  }

  DataSource dataSource() {
    return proxy(
        DataSource.class,
        (proxy, method, args) -> {
          if ("getConnection".equals(method.getName())) {
            if (failure != null) {
              throw failure;
            }
            return connection();
          }
          return defaultFor(proxy, method.getName(), args);
        });
  }

  private Connection connection() {
    return proxy(
        Connection.class,
        (proxy, method, args) -> {
          if ("prepareStatement".equals(method.getName())) {
            return statement((String) args[0]);
          }
          return defaultFor(proxy, method.getName(), args);
        });
  }

  private PreparedStatement statement(final String sql) {
    final Map<Integer, Object> params = new HashMap<>();
    executed.add(new Executed(sql, params));
    return proxy(
        PreparedStatement.class,
        (proxy, method, args) -> {
          final String name = method.getName();
          if (name.startsWith("set") && args != null && args.length >= 2) {
            params.put((Integer) args[0], "setNull".equals(name) ? null : args[1]);
            return null;
          }
          if ("executeUpdate".equals(name)) {
            return 1;
          }
          if ("executeQuery".equals(name)) {
            final List<Map<String, Object>> rows =
                queryResults.isEmpty() ? List.of() : queryResults.poll();
            return resultSet(rows);
          }
          return defaultFor(proxy, name, args);
        });
  }

  private ResultSet resultSet(final List<Map<String, Object>> rows) {
    final int[] cursor = {-1};
    return proxy(
        ResultSet.class,
        (proxy, method, args) -> {
          final String name = method.getName();
          if ("next".equals(name)) {
            cursor[0]++;
            return cursor[0] < rows.size();
          }
          if (name.startsWith("get") && args != null && args.length == 1) {
            final Object value = rows.get(cursor[0]).get((String) args[0]);
            return switch (name) {
              case "getBoolean" -> value != null && (Boolean) value;
              case "getInt" -> value == null ? 0 : (Integer) value;
              default -> value;
            };
          }
          return defaultFor(proxy, name, args);
        });
  }

  private static Object defaultFor(final Object proxy, final String name, final Object[] args) {
    return switch (name) {
      case "close" -> null;
      case "toString" -> "FakeJdbc proxy";
      case "hashCode" -> System.identityHashCode(proxy);
      case "equals" -> proxy == args[0];
      default -> throw new UnsupportedOperationException(name);
    };
  }

  @SuppressWarnings("unchecked")
  private static <T> T proxy(final Class<T> type, final InvocationHandler handler) {
    return (T)
        Proxy.newProxyInstance(
            FakeJdbc.class.getClassLoader(),
            new Class<?>[] {type},
            (proxy, method, args) -> {
              try {
                return handler.invoke(proxy, method, args);
              } catch (final InvocationTargetException exception) {
                throw exception.getCause();
              }
            });
  }

  /** Fila de ejemplo con las columnas de la tabla aplicaciones. */
  static Map<String, Object> row(final String id, final String nombre) {
    final Map<String, Object> row = new HashMap<>();
    row.put("id", id);
    row.put("nombre", nombre);
    row.put("proveedor", "Microsoft");
    row.put("categoria", "DESARROLLO");
    row.put("lenguaje_principal", "TypeScript");
    row.put("lenguaje_secundario", "JavaScript");
    row.put("usa_bd", false);
    row.put("requiere_conexion_red", true);
    row.put("num_bits", 64);
    row.put("sistema_operativo", "MULTIPLATAFORMA");
    row.put("requisitos_hardware", "4 GB de RAM");
    row.put("licencia", "OPEN_SOURCE");
    row.put("precio", new BigDecimal("0.00"));
    row.put("descripcion", "Editor de codigo");
    row.put("web", "https://code.visualstudio.com");
    row.put("correo", "soporte@ejemplo.com");
    row.put("tamano_instalador_mb", new BigDecimal("95.50"));
    return row;
  }
}
