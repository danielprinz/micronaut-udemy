package com.danielprinz.udemy.broker.persistence.jpa;

import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.danielprinz.udemy.broker.persistence.model.QuoteEntity;
import com.danielprinz.udemy.broker.persistence.model.SymbolEntity;

import io.micronaut.context.annotation.Requires;
import io.micronaut.context.env.Environment;
import io.micronaut.context.event.StartupEvent;
import io.micronaut.runtime.event.annotation.EventListener;

/**
 * Used to insert data into db on startup
 */
@Singleton
@Requires(notEnv = Environment.TEST)
public class TestDataProvider {

  private static final Logger LOG = LoggerFactory.getLogger(TestDataProvider.class);
  private static final ThreadLocalRandom RANDOM = ThreadLocalRandom.current();
  private final SymbolsRepository symbols;
  private final QuotesRepository quotes;

  public TestDataProvider(final SymbolsRepository symbols,
    final QuotesRepository quotes) {
    this.symbols = symbols;
    this.quotes = quotes;
  }

  @EventListener
  public void init(StartupEvent event) {
    if (symbols.findAll().isEmpty()) {
      LOG.info("Adding test data as empty database was found!!!");
      Stream.of("AAPL", "AMZN", "FB", "TSLA")
        .map(SymbolEntity::new)
        .forEach(symbols::save);
    }
    if (quotes.findAll().isEmpty()) {
      LOG.info("Adding test data as empty database was found!!!");
      symbols.findAll().forEach(symbol -> {
        var quote = new QuoteEntity();
        quote.setSymbol(symbol);
        quote.setAsk(randomValue());
        quote.setBid(randomValue());
        quote.setLastPrice(randomValue());
        quote.setVolume(randomValue());
        quotes.save(quote);
      });
    }
  }

  private BigDecimal randomValue() {
    return BigDecimal.valueOf(RANDOM.nextDouble(1, 100));
  }
}
