package com.danielprinz.udemy.data.jdbc;

import java.math.BigDecimal;

import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.data.model.naming.NamingStrategies;

@MappedEntity(value = "transactions", namingStrategy = NamingStrategies.UnderScoreSeparatedLowerCase.class)
public class Transaction {

  @Id
  private long transactionId;
  private final String user;
  private final String symbol;
  private final BigDecimal modification;

  public Transaction(final String user, final String symbol, final BigDecimal modification) {
    this.user = user;
    this.symbol = symbol;
    this.modification = modification;
  }

  public Transaction(long transactionId, String user, String symbol, BigDecimal modification) {
    this.transactionId = transactionId;
    this.user = user;
    this.symbol = symbol;
    this.modification = modification;
  }

  public long getTransactionId() {
    return transactionId;
  }

  public void setTransactionId(long transactionId) {
    this.transactionId = transactionId;
  }

  public String getUser() {
    return user;
  }

  public String getSymbol() {
    return symbol;
  }

  public BigDecimal getModification() {
    return modification;
  }

  @Override
  public String toString() {
    return "Transaction{" +
        "transactionId=" + transactionId +
        ", user='" + user + '\'' +
        ", symbol='" + symbol + '\'' +
        ", modification=" + modification +
        '}';
  }
}
