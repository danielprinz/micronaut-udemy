package com.danielprinz.udemy.quotes.external;

import java.math.BigDecimal;

import io.micronaut.core.annotation.Introspected;

@Introspected
public class ExternalQuote {

  private String symbol;
  private BigDecimal lastPrice;
  private BigDecimal volume;

  public ExternalQuote() {
  }

  public ExternalQuote(final String symbol, final BigDecimal lastPrice, final BigDecimal volume) {
    this.symbol = symbol;
    this.lastPrice = lastPrice;
    this.volume = volume;
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

  public BigDecimal getVolume() {
    return volume;
  }

  public void setVolume(final BigDecimal volume) {
    this.volume = volume;
  }

  @Override
  public String toString() {
    return "ExternalQuote{" +
      "symbol='" + symbol + '\'' +
      ", lastPrice=" + lastPrice +
      ", volume=" + volume +
      '}';
  }
}
