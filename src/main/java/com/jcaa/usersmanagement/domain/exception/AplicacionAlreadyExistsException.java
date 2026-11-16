package com.jcaa.usersmanagement.domain.exception;

public final class AplicacionAlreadyExistsException extends DomainException {

  private static final String MESSAGE_BY_NAME = "An application named '%s' already exists.";

  private AplicacionAlreadyExistsException(final String message) {
    super(message);
  }

  public static AplicacionAlreadyExistsException becauseNombreAlreadyExists(final String nombre) {
    return new AplicacionAlreadyExistsException(String.format(MESSAGE_BY_NAME, nombre));
  }
}
