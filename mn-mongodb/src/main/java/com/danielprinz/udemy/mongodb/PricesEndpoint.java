package com.danielprinz.udemy.mongodb;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoCollection;

import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.reactivex.Flowable;
import io.reactivex.Single;

@Controller("/prices")
public class PricesEndpoint {

  private static final Logger LOG = LoggerFactory.getLogger(PricesEndpoint.class);
  private final MongoClient mongoClient;

  public PricesEndpoint(final MongoClient mongoClient) {
    this.mongoClient = mongoClient;
  }

  @Get("/")
  public Flowable<Document> fetch() {
    var collection = getCollection();
    return Flowable.fromPublisher(collection.find());
  }

  @Post("/")
  public Single<InsertOneResult> insert(@Body ObjectNode json) {
    var collection = getCollection();
    final Document doc = Document.parse(json.toString());
    LOG.info("Insert {}", doc);
    return Single.fromPublisher(collection.insertOne(doc));
  }

  private MongoCollection<Document> getCollection() {
    return mongoClient.getDatabase("prices").getCollection("example");
  }
}
