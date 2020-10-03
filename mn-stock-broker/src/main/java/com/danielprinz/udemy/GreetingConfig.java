package com.danielprinz.udemy;

import javax.validation.constraints.NotBlank;

import io.micronaut.context.annotation.ConfigurationInject;
import io.micronaut.context.annotation.ConfigurationProperties;
import lombok.Getter;

@ConfigurationProperties("hello.config.greeting")
public class GreetingConfig {

  @Getter
  private final String de;
  @Getter
  private final String en;

  @ConfigurationInject
  public GreetingConfig(@NotBlank final String de, @NotBlank final String en) {
    this.de = de;
    this.en = en;
  }

}
