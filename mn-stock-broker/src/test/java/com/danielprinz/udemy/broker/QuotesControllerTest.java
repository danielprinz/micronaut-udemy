package com.danielprinz.udemy.broker;

import static io.micronaut.http.HttpRequest.GET;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.danielprinz.udemy.broker.model.CustomError;
import com.danielprinz.udemy.broker.model.Quote;
import com.danielprinz.udemy.broker.model.Symbol;
import com.danielprinz.udemy.broker.store.InMemoryStore;

import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.runtime.EmbeddedApplication;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;

@MicronautTest
public class QuotesControllerTest {

  private static final Logger LOG = LoggerFactory.getLogger(QuotesControllerTest.class);

  @Inject
  EmbeddedApplication application;

  @Inject
  @Client("/") RxHttpClient client;

  @Inject
  InMemoryStore store;

  @Test
  void returnsQuotePerSymbol() {
    final Quote apple = generateQuote("APPL");
    store.update(apple);
    final Quote amazon = generateQuote("AMZN");
    store.update(amazon);

    final Quote appleResult = client.toBlocking().retrieve(GET("/quotes/APPL"), Quote.class);
    LOG.info("Result: {}", appleResult);
    assertThat(apple).isEqualToComparingFieldByField(appleResult);

    final Quote amazonResult = client.toBlocking().retrieve(GET("/quotes/AMZN"), Quote.class);
    LOG.info("Result: {}", amazonResult);
    assertThat(amazon).isEqualToComparingFieldByField(amazonResult);
  }

  @Test
  void returnsNotFoundOnUnsupportedSymbol() {
    try {
      client.toBlocking().exchange(GET("/quotes/UNSUPPORTED"),
        Argument.of(Quote.class), Argument.of(CustomError.class));
    } catch (HttpClientResponseException e) {
      assertEquals(HttpStatus.NOT_FOUND, e.getResponse().getStatus());
      Optional<CustomError> jsonError = e.getResponse().getBody(CustomError.class);
      assertTrue(jsonError.isPresent());
      LOG.debug("Custom Error: {}", jsonError.get());
      assertEquals(404, jsonError.get().getStatus());
      assertEquals("NOT_FOUND", jsonError.get().getError());
      assertEquals("quote for symbol not available", jsonError.get().getMessage());
      assertEquals("/quotes/UNSUPPORTED", jsonError.get().getPath());
    }
  }

  public Quote generateQuote(final String symbol) {
    return Quote.builder().symbol(new Symbol(symbol)).bid(randomValue())
      .ask(randomValue())
      .lastPrice(randomValue())
      .volume(randomValue())
      .build();
  }

  private BigDecimal randomValue() {
    return BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(1, 100));
  }

}
