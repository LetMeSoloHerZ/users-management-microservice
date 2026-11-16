package com.jcaa.usersmanagement.infrastructure.adapter.persistence.exception;

public final class AplicacionPersistenceException extends RuntimeException {

  private static final String MESSAGE_SAVE = "Failed to save application with ID: '%s'.";
  private static final String MESSAGE_UPDATE = "Failed to update application with ID: '%s'.";
  private static final String MESSAGE_FIND = "Failed to find application with ID: '%s'.";
  private static final String MESSAGE_FIND_BY_NAME = "Failed to find application named: '%s'.";
  private static final String MESSAGE_ALL = "Failed to retrieve all applications.";
  private static final String MESSAGE_DELETE = "Failed to delete application with ID: '%s'.";

  private AplicacionPersistenceException(final String message, final Throwable cause) {
    super(message, cause);
  }

  public static AplicacionPersistenceException becauseSaveFailed(
      final String id, final Throwable cause) {
    return new AplicacionPersistenceException(String.format(MESSAGE_SAVE, id), cause);
  }

  public static AplicacionPersistenceException becauseUpdateFailed(
      final String id, final Throwable cause) {
    return new AplicacionPersistenceException(String.format(MESSAGE_UPDATE, id), cause);
  }

  public static AplicacionPersistenceException becauseFindByIdFailed(
      final String id, final Throwable cause) {
    return new AplicacionPersistenceException(String.format(MESSAGE_FIND, id), cause);
  }

  public static AplicacionPersistenceException becauseFindByNombreFailed(
      final String nombre, final Throwable cause) {
    return new AplicacionPersistenceException(String.format(MESSAGE_FIND_BY_NAME, nombre), cause);
  }

  public static AplicacionPersistenceException becauseFindAllFailed(final Throwable cause) {
    return new AplicacionPersistenceException(MESSAGE_ALL, cause);
  }

  public static AplicacionPersistenceException becauseDeleteFailed(
      final String id, final Throwable cause) {
    return new AplicacionPersistenceException(String.format(MESSAGE_DELETE, id), cause);
  }
}
