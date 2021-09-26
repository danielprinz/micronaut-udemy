package com.danielprinz.udemy.redis;

import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.runtime.EmbeddedApplication;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.inject.Inject;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest
class MnRateLimitTest {

  private static final Logger LOG = LoggerFactory.getLogger(MnRateLimitTest.class);

  @Inject
  EmbeddedApplication application;

  @Inject
  @Client("/time")
  HttpClient client;

  @Test
  void testItWorks() {
    Assertions.assertTrue(application.isRunning());
  }

  @Test
  void triggerRateLimit() {
    IntStream.rangeClosed(0, RateLimitedTimeEndpoint.QUOTA_PER_MINUTE)
    .forEach(count ->
      LOG.debug(retrieve())
    );
    var result = retrieve();
    LOG.debug(result);
    assertEquals("Rate limit reached EXAMPLE::UTC 10/10", result);
  }

  private String retrieve() {
    return client.toBlocking().retrieve("/utc");
  }

}
