package com.danielprinz.udemy;

import javax.inject.Inject;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.micronaut.runtime.EmbeddedApplication;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;

@MicronautTest
public class MnFundingTest {

  @Inject
  EmbeddedApplication application;

  @Test
  void testItWorks() {
    Assertions.assertTrue(application.isRunning());
  }

}
