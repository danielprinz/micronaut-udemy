package com.danielprinz.udemy.di;

import com.danielprinz.udemy.hello.HelloWorldService;
import io.micronaut.runtime.Micronaut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomBeanInjection {

  private static final Logger LOG = LoggerFactory.getLogger(CustomBeanInjection.class);

  public static void main(String[] args) {
    var context = Micronaut.run(CustomBeanInjection.class, args);
    final var helloWorldService = context.getBean(HelloWorldService.class);
    LOG.info("Message from Service: {}", helloWorldService.helloFromService());
    final var injectMeService = context.getBean(InjectMeService.class);
    LOG.info("Custom Injected Bean: {}", injectMeService.className());
  }
}
