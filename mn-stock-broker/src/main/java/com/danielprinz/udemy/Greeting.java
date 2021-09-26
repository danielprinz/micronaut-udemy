package com.danielprinz.udemy;

import java.math.BigDecimal;
import java.time.Instant;

import io.micronaut.core.annotation.Introspected;
import lombok.Data;

@Data
@Introspected
public class Greeting {

  final String myText = "Hello World";
  final BigDecimal id = BigDecimal.valueOf(123456789);
  final Instant timeUTC = Instant.now();

}
