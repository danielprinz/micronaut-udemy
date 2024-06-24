package com.danielprinz.udemy.jokes;

import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.serde.annotation.Serdeable;

@Serdeable
@MappedEntity
public record Joke(
  @Id
  String id,
  String content
) {
}
