package com.danielprinz.udemy.server;

import io.micronaut.scheduling.annotation.Scheduled;
import io.micronaut.websocket.WebSocketBroadcaster;
import io.micronaut.websocket.WebSocketSession;
import io.micronaut.websocket.annotation.OnClose;
import io.micronaut.websocket.annotation.OnMessage;
import io.micronaut.websocket.annotation.OnOpen;
import io.micronaut.websocket.annotation.ServerWebSocket;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;

@ServerWebSocket("/ws/stocks/prices/{symbols}")
public class QuotesWebSocket {
    private static final Logger LOGGER = LoggerFactory.getLogger(QuotesWebSocket.class);

    private final WebSocketBroadcaster broadcaster;
    private final ConcurrentMap<String, List<String>> watchlist = new ConcurrentHashMap<>();

    public QuotesWebSocket(final WebSocketBroadcaster broadcaster) {
        this.broadcaster = broadcaster;
    }

    @OnOpen
    public void onOpen(List<String> symbols, WebSocketSession session) {
        LOGGER.debug("Session opened {}", session.getId());
        watchlist.put(session.getId(), symbols);
    }

    @OnMessage
    public Publisher<String> onMessage(String message, WebSocketSession session) {
        LOGGER.debug("Message received {}: '{}'", session.getId(), message);
        return session.send(message);
    }

    @OnClose
    public void onClose(WebSocketSession session) {
        LOGGER.debug("Session closed {}", session.getId());
        watchlist.remove(session.getId());
    }

    @Scheduled(fixedDelay = "${update.interval}")
    public void quoteUpdate() {
        watchlist.values().stream()
                .flatMap(List::stream)
                .distinct()
                .map(symbol -> new PriceUpdate(symbol, randomValue()))
                .forEach(priceUpdate -> broadcaster.broadcastSync(priceUpdate, isWatching(watchlist, priceUpdate.symbol())));
    }


    private Predicate<WebSocketSession> isWatching(ConcurrentMap<String, List<String>> watchlist, String symbol) {
        return session -> watchlist.get(session.getId()).contains(symbol);
    }

    private BigDecimal randomValue() {
        return BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(1500, 2000))
                .setScale(2, RoundingMode.HALF_UP);
    }
}
