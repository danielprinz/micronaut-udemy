package com.danielprinz.udemy.websockets.simple;

import java.math.BigDecimal;

public class PriceUpdate {
  private final String symbol;
  private final BigDecimal price;

  public PriceUpdate(final String symbol, final BigDecimal price) {
    this.symbol = symbol;
    this.price = price;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public String getSymbol() {
    return symbol;
  }
}
