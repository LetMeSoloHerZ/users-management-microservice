package com.jcaa.usersmanagement.domain.exception;

public final class AplicacionNotFoundException extends DomainException {

  private static final String MESSAGE_BY_ID = "The application with id '%s' was not found.";

  private AplicacionNotFoundException(final String message) {
    super(message);
  }

  public static AplicacionNotFoundException becauseIdWasNotFound(final String aplicacionId) {
    return new AplicacionNotFoundException(String.format(MESSAGE_BY_ID, aplicacionId));
  }
}
