package com.danielprinz.udemy;

import java.util.HashMap;
import java.util.UUID;

import jakarta.inject.Singleton;

import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.javafaker.Faker;

import io.micronaut.scheduling.annotation.Scheduled;

@Singleton
public class TestDataProvider {

  private static final Logger LOG = LoggerFactory.getLogger(TestDataProvider.class);
  private static final Faker FAKER = new Faker();

  private final RestHighLevelClient client;

  public TestDataProvider(final RestHighLevelClient client) {
    this.client = client;
  }

  @Scheduled(fixedDelay = "10s")
  void insertDocument() {
    var document = new HashMap<String, String>();
    document.put("first_name", FAKER.name().firstName());
    document.put("last_name", FAKER.name().lastName());

    final IndexRequest indexRequest = new IndexRequest()
        .index(Constants.INDEX)
        .id(UUID.randomUUID().toString())
        .source(document, XContentType.JSON);
    client.indexAsync(indexRequest, RequestOptions.DEFAULT, new ActionListener<>() {
      @Override
      public void onResponse(final IndexResponse indexResponse) {
        LOG.debug("Added document {} with id {}", document, indexResponse.getId());
      }

      @Override
      public void onFailure(final Exception e) {
        LOG.error("Failed to insert document: ", e);
      }
    });
  }
}
