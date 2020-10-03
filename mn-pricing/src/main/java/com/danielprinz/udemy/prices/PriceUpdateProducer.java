package com.danielprinz.udemy.prices;

import java.util.List;

import org.apache.kafka.clients.producer.RecordMetadata;

import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.configuration.kafka.annotation.Topic;
import io.reactivex.Flowable;

@KafkaClient(batch = true)
public interface PriceUpdateProducer {

  @Topic("price_update")
  Flowable<RecordMetadata> send(List<PriceUpdate> prices);

}
