package com.danielprinz.udemy.prices;

import java.math.BigDecimal;

import io.micronaut.core.annotation.Introspected;

@Introspected
public class PriceUpdate {

  private String symbol;
  private BigDecimal lastPrice;

  public PriceUpdate() {
  }

  public PriceUpdate(final String symbol, final BigDecimal lastPrice) {
    this.symbol = symbol;
    this.lastPrice = lastPrice;
  }

  public String getSymbol() {
    return symbol;
  }

  public void setSymbol(final String symbol) {
    this.symbol = symbol;
  }

  public BigDecimal getLastPrice() {
    return lastPrice;
  }

  public void setLastPrice(final BigDecimal lastPrice) {
    this.lastPrice = lastPrice;
  }

  @Override
  public String toString() {
    return "PriceUpdate{" +
      "symbol='" + symbol + '\'' +
      ", lastPrice=" + lastPrice +
      '}';
  }
}
