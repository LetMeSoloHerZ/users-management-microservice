package com.jcaa.usersmanagement.domain.valueobject;

import com.jcaa.usersmanagement.domain.exception.InvalidAplicacionException;
import java.math.BigDecimal;
import java.math.RoundingMode;

/** Tamano del instalador expresado en megabytes (MB). */
public record TamanoInstalador(BigDecimal value) {

  private static final BigDecimal MAXIMUM_VALUE = new BigDecimal("99999999.99");

  public TamanoInstalador {
    if (value == null) {
      throw InvalidAplicacionException.becauseInstallerSizeIsInvalid("null");
    }
    final BigDecimal scaledValue = value.setScale(2, RoundingMode.HALF_UP);
    if (scaledValue.signum() <= 0 || scaledValue.compareTo(MAXIMUM_VALUE) > 0) {
      throw InvalidAplicacionException.becauseInstallerSizeIsInvalid(value.toPlainString());
    }
    value = scaledValue;
  }

  @Override
  public String toString() {
    return value.toPlainString() + " MB";
  }
}
