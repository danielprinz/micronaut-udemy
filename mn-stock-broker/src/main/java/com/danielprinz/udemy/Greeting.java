package com.danielprinz.udemy;

import java.math.BigDecimal;
import java.time.Instant;

import lombok.Data;

@Data
public class Greeting {

  final String myText = "Hello World";
  final BigDecimal id = BigDecimal.valueOf(123456789);
  final Instant timeUTC = Instant.now();

}
