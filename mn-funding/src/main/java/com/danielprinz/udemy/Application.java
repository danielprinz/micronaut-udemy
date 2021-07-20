package com.danielprinz.udemy;

import io.micronaut.runtime.Micronaut;
import org.slf4j.bridge.SLF4JBridgeHandler;

public class Application {

  public static void main(String[] args) {
    // Bridge JUL to Slf4j
    SLF4JBridgeHandler.removeHandlersForRootLogger();
    SLF4JBridgeHandler.install();
    Micronaut.run(Application.class, args);
  }
}
