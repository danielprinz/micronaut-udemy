package com.danielprinz.udemy.hello;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import jakarta.inject.Singleton;

@Singleton
@Controller("/hello")
public class HelloWorldController {

  private final MyService service;

  public HelloWorldController(final MyService service) {
    this.service = service;
  }

  @Get(produces = MediaType.TEXT_PLAIN)
  public String helloWorld() {
    return service.helloFromService();
  }
}
