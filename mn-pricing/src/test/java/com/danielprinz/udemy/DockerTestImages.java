package com.danielprinz.udemy;

import org.testcontainers.utility.DockerImageName;

public class DockerTestImages {

  public static DockerImageName KAFKA = DockerImageName.parse("confluentinc/cp-kafka:6.1.2");
}
