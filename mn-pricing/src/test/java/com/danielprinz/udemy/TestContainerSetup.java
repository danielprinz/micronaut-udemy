package com.danielprinz.udemy;

import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
class TestContainerSetup {

  private static final Logger LOG = LoggerFactory.getLogger(TestContainerSetup.class);

  @Rule
  public KafkaContainer kafka = new KafkaContainer(DockerTestImages.KAFKA);

  @Test
  void setupWorks() {
    kafka.start();
    LOG.debug("Bootstrap Servers: {}", kafka.getBootstrapServers());
    kafka.stop();
  }

}
