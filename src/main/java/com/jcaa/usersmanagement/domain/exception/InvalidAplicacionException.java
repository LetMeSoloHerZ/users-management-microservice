package com.jcaa.usersmanagement.domain.exception;

public final class InvalidAplicacionException extends DomainException {

  private InvalidAplicacionException(final String message) {
    super(message);
  }

  public static InvalidAplicacionException becauseFieldIsEmpty(final String field) {
    return new InvalidAplicacionException(String.format("The field '%s' must not be empty.", field));
  }

  public static InvalidAplicacionException becauseFieldIsTooShort(
      final String field, final int minimumLength) {
    return new InvalidAplicacionException(
        String.format("The field '%s' must have at least %d characters.", field, minimumLength));
  }

  public static InvalidAplicacionException becauseFieldIsTooLong(
      final String field, final int maximumLength) {
    return new InvalidAplicacionException(
        String.format("The field '%s' must not exceed %d characters.", field, maximumLength));
  }

  public static InvalidAplicacionException becauseIdFormatIsInvalid(final String value) {
    return new InvalidAplicacionException(
        String.format("The application id '%s' is not a valid UUID.", value));
  }

  public static InvalidAplicacionException becauseEmailFormatIsInvalid(final String value) {
    return new InvalidAplicacionException(
        String.format("The email '%s' does not have a valid format.", value));
  }

  public static InvalidAplicacionException becauseWebFormatIsInvalid(final String value) {
    return new InvalidAplicacionException(
        String.format("The web address '%s' must be a valid http or https URL.", value));
  }

  public static InvalidAplicacionException becausePriceIsInvalid(final String value) {
    return new InvalidAplicacionException(
        String.format("The price '%s' must be a number greater than or equal to zero.", value));
  }

  public static InvalidAplicacionException becauseInstallerSizeIsInvalid(final String value) {
    return new InvalidAplicacionException(
        String.format("The installer size '%s' must be a number greater than zero.", value));
  }

  public static InvalidAplicacionException becauseBitsAreInvalid(final int value) {
    return new InvalidAplicacionException(
        String.format("The number of bits '%d' is invalid. Allowed values: 32, 64.", value));
  }

  public static InvalidAplicacionException becauseValueIsNotAllowed(
      final String field, final String value, final String allowedValues) {
    return new InvalidAplicacionException(
        String.format(
            "The value '%s' is not valid for '%s'. Allowed values: %s.", value, field, allowedValues));
  }

  public static InvalidAplicacionException becauseSecondaryLanguageEqualsPrimary(
      final String language) {
    return new InvalidAplicacionException(
        String.format(
            "The secondary language must be different from the primary language ('%s').", language));
  }

  public static InvalidAplicacionException becauseLicenseRequiresZeroPrice(final String license) {
    return new InvalidAplicacionException(
        String.format("A %s license requires the price to be zero.", license));
  }

  public static InvalidAplicacionException becauseLicenseRequiresPositivePrice(
      final String license) {
    return new InvalidAplicacionException(
        String.format("A %s license requires a price greater than zero.", license));
  }
}
