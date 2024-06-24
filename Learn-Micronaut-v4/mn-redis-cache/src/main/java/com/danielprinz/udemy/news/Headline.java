package com.danielprinz.udemy.news;

import io.micronaut.serde.annotation.Serdeable;
import java.time.Month;
import java.util.List;

@Serdeable
public record Headline(
  Month month,
  List<String> content
) {
}
