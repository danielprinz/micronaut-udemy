package com.danielprinz.udemy.jokes;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.json.tree.JsonNode;
import jakarta.inject.Singleton;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

@Singleton
public class JokesService {

  private final JokesRepository repository;
  private final HttpClient client;

  public JokesService(
    JokesRepository repository,
    @Client("https://api.chucknorris.io/")
    HttpClient client
  ) {
    this.repository = repository;
    this.client = client;
  }

  public Publisher<Joke> list() {
    return repository.findAll();
  }

  public Mono<Joke> generateAndPersistRandomJoke() {
    return Mono.from(client.retrieve(HttpRequest.GET("/jokes/random"), JsonNode.class))
      .map(json -> new Joke(
        json.get("id").getStringValue(),
        json.get("value").getStringValue()
      ))
      .flatMap(joke -> Mono.from(repository.save(joke)));
  }

}
