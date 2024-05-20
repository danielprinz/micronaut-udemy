package com.danielprinz.udemy.hello;

import jakarta.inject.Singleton;

@Singleton
public class HelloMicronautService implements MyService {

  @Override
  public String helloFromService() {
    return "Hello Micronaut!";
  }

}
