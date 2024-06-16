package com.danielprinz.udemy.ratelimit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@MicronautTest
class TimeControllerTest {

  private static final Logger LOG = LoggerFactory.getLogger(TimeControllerTest.class);

  @Inject
  @Client("/time")
  HttpClient client;

  @Test
  void triggerRateLimitOnTimeEndpoint() {
    final var path = "/";
    IntStream.rangeClosed(0, 9)
      .forEach(count ->
        retrieve(path)
      );

    var result = assertThrows(HttpClientResponseException.class,
      () -> retrieve(path)
    );
    assertEquals("Client '/time': Rate limit reached TIMECONTROLLER::TIME::test 10/10", result.getMessage());
    assertEquals(HttpStatus.TOO_MANY_REQUESTS, result.getStatus());
  }

  @Test
  void triggerRateLimitOnTimeUtcEndpoint() {
    final var path = "/utc";
    IntStream.rangeClosed(0, 9)
      .forEach(count ->
        retrieve(path)
      );
    var result = assertThrows(HttpClientResponseException.class,
      () -> retrieve(path)
    );
    assertEquals("Client '/time': Rate limit reached TIMECONTROLLER::TIMEUTC 10/10", result.getMessage());
    assertEquals(HttpStatus.TOO_MANY_REQUESTS, result.getStatus());
  }

  private void retrieve(String path) {
    client.toBlocking().exchange(HttpRequest.GET(path)
      .header("x-client-id", "test")
    );
  }

}
