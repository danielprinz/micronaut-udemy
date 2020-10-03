package com.danielprinz.udemy.data.jdbc;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.micronaut.scheduling.annotation.Scheduled;

@Singleton
public class DummyDataProvider {

  private static final Logger LOG = LoggerFactory.getLogger(DummyDataProvider.class);
  private final TransactionsRepository transactions;

  public DummyDataProvider(final TransactionsRepository transactions) {
    this.transactions = transactions;
  }

  @Scheduled(fixedDelay = "1s")
  void generate() {
    transactions.save(new Transaction(UUID.randomUUID().toString(), "SYMBOL", randomValue()));
    LOG.info("Content Size: {}", transactions.findAll().size());
  }

  private BigDecimal randomValue() {
    return BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(1, 100));
  }
}
