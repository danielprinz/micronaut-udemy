package com.danielprinz.udemy.jokes;

import io.micronaut.data.mongodb.annotation.MongoRepository;
import io.micronaut.data.repository.reactive.ReactiveStreamsCrudRepository;

@MongoRepository
public interface JokesRepository extends ReactiveStreamsCrudRepository<Joke, String> {
}
