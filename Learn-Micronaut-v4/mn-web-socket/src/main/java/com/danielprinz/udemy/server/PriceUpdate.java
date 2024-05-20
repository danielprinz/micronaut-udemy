package com.danielprinz.udemy.server;

import io.micronaut.serde.annotation.Serdeable;

import java.math.BigDecimal;

@Serdeable.Serializable
@Serdeable.Deserializable
public record PriceUpdate(String symbol, BigDecimal price) {
}