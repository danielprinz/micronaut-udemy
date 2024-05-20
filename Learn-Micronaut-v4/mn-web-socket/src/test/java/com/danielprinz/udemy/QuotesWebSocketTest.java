package com.danielprinz.udemy;

import com.danielprinz.udemy.client.QuotesClient;
import com.danielprinz.udemy.server.PriceUpdate;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.websocket.WebSocketClient;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.net.URI;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
public class QuotesWebSocketTest {
    @Inject WebSocketClient quotesClient;
    @Inject EmbeddedServer server;

    @Test
    public void subscribeToQuotesSocket_setsPrice() throws Exception {
        try (var client = createWebSocketClient("AMZN")) {
            await().untilAsserted(() -> {
                var price = client.getPrice("AMZN");
                assertTrue(price.isPresent());
            });
        }
    }

    @Test
    public void subscribeToQuotesSocket_updatesPrice() throws Exception {
        try (var client = createWebSocketClient("AMZN")) {
            await().until(() -> client.getPrice("AMZN").isPresent());
            BigDecimal initialPrice = client.getPrice("AMZN").get();

            await().untilAsserted(() -> {
                var priceUpdate = client.getPrice("AMZN");
                assertNotEquals(initialPrice, priceUpdate.get());
            });
        }
    }

    @Test
    public void sendPriceUpdate_receivesPriceUpdate() throws Exception {
        try (var client = createWebSocketClient("AMZN")) {
            client.send(new PriceUpdate("RNDM", BigDecimal.TEN));
            await().until(() -> client.getPrice("RNDM").isPresent() && client.getPrice("RNDM").get().equals(BigDecimal.TEN));
        }
    }

    private QuotesClient createWebSocketClient(String... symbols) {
        URI quotesSocketUri = UriBuilder.of("ws://localhost")
                .port(server.getPort())
                .path("ws/stocks/prices/" + String.join(",", symbols))
                .build();

        Publisher<QuotesClient> webSocketClient = quotesClient.connect(QuotesClient.class, quotesSocketUri);
        return Flux.from(webSocketClient).blockFirst();
    }
}
