package com.danielprinz.udemy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.micronaut.context.ApplicationContext;
import io.micronaut.runtime.Micronaut;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

@OpenAPIDefinition(
  info = @Info(
    title = "mn-stock-broker",
    version = "0.1",
    description = "Udemy Micronaut Course",
    license = @License(name = "MIT")
  )
)
public class Application {

  private static final Logger LOG = LoggerFactory.getLogger(Application.class);

  public static void main(String[] args) {
    final ApplicationContext context = Micronaut.run(Application.class);
    final HelloWorldService service = context.getBean(HelloWorldService.class);
    LOG.info(service.sayHi());
  }
}
