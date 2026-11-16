package com.jcaa.usersmanagement.domain.valueobject;

import com.jcaa.usersmanagement.domain.exception.InvalidAplicacionException;
import java.math.BigDecimal;
import java.math.RoundingMode;

public record Precio(BigDecimal value) {

  private static final BigDecimal MAXIMUM_VALUE = new BigDecimal("99999999.99");

  public Precio {
    if (value == null) {
      throw InvalidAplicacionException.becausePriceIsInvalid("null");
    }
    final BigDecimal scaledValue = value.setScale(2, RoundingMode.HALF_UP);
    if (scaledValue.signum() < 0 || scaledValue.compareTo(MAXIMUM_VALUE) > 0) {
      throw InvalidAplicacionException.becausePriceIsInvalid(value.toPlainString());
    }
    value = scaledValue;
  }

  public boolean isZero() {
    return value.signum() == 0;
  }

  @Override
  public String toString() {
    return value.toPlainString();
  }
}
