package com.danielprinz.udemy.websockets.simple;

import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;

import javax.inject.Singleton;

import io.micronaut.scheduling.annotation.Scheduled;
import io.micronaut.websocket.WebSocketBroadcaster;

@Singleton
public class PricePush {

  private final WebSocketBroadcaster broadcaster;

  public PricePush(final WebSocketBroadcaster broadcaster) {
    this.broadcaster = broadcaster;
  }

  @Scheduled(fixedDelay = "5s")
  public void push() {
    broadcaster.broadcastSync(
      new PriceUpdate("AMZN", randomValue())
    );
  }

  private BigDecimal randomValue() {
    return BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(1500, 2000));
  }

}
