package com.danielprinz.udemy.jokes;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import org.reactivestreams.Publisher;

@Controller("/jokes")
public class JokesController {

  private final JokesService service;

  public JokesController(JokesService service) {
    this.service = service;
  }

  @Get
  Publisher<Joke> list() {
    return service.list();
  }

  @Get("/random")
  Publisher<Joke> generateRandomJoke() {
    return service.generateAndPersistRandomJoke();
  }
}
