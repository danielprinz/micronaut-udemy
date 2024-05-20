package com.danielprinz.udemy.hello;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

@Controller("/hello")
public class HelloWorldController {

  @Get(produces = MediaType.TEXT_PLAIN)
  public String helloWorld() {
    return "Hello world!";
  }
}
