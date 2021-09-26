package com.danielprinz.udemy.prices;

import java.util.List;

import org.apache.kafka.clients.producer.RecordMetadata;

import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.configuration.kafka.annotation.Topic;
import reactor.core.publisher.Flux;

@KafkaClient(batch = true)
public interface PriceUpdateProducer {

  @Topic("price_update")
  Flux<RecordMetadata> send(List<PriceUpdate> prices);

}
