package com.danielprinz.udemy.broker;

import static io.micronaut.http.HttpRequest.GET;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.LinkedHashMap;
import java.util.List;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;

import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.runtime.EmbeddedApplication;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;

@MicronautTest
public class MarketsControllerTest {

  @Inject
  EmbeddedApplication application;

  @Inject
  @Client("/") RxHttpClient client;

  @Test
  void returnsListOfMarkets() {
    final List result = client.retrieve(GET("/markets"), List.class)
      .blockingSingle();
    assertEquals(7, result.size());
    final List<LinkedHashMap<String, String>> markets = result;
    assertThat(markets)
      .extracting(entry -> entry.get("value"))
      .containsExactlyInAnyOrder("AAPL", "AMZN", "FB", "GOOG", "MSFT", "NFLX", "TSLA");
  }

}
