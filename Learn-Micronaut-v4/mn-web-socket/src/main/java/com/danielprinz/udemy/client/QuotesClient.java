package com.danielprinz.udemy.client;

import com.danielprinz.udemy.server.PriceUpdate;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.websocket.annotation.ClientWebSocket;
import io.micronaut.websocket.annotation.OnMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@ClientWebSocket("/ws/stocks/prices")
public abstract class QuotesClient implements AutoCloseable {
    private static final Logger LOGGER = LoggerFactory.getLogger(QuotesClient.class);
    private final Map<String, BigDecimal> currentPrices = new HashMap<>();

    public Optional<BigDecimal> getPrice(String symbol) {
        return Optional.ofNullable(currentPrices.get(symbol));
    }

    public abstract void send(@NonNull PriceUpdate priceUpdate);

    @OnMessage
    protected void onMessage(PriceUpdate price) {
        LOGGER.debug("Message received: " + price.symbol() + ": " + price.price());
        currentPrices.put(price.symbol(), price.price());
    }
}
