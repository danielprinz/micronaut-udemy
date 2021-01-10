package com.danielprinz.udemy.es;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.asyncsearch.AsyncSearchResponse;
import org.elasticsearch.client.asyncsearch.SubmitAsyncSearchRequest;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.danielprinz.udemy.Constants;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;

@Controller("/data")
public class DataEndpoint {

  private static final Logger LOG = LoggerFactory.getLogger(DataEndpoint.class);
  private final RestHighLevelClient client;

  public DataEndpoint(final RestHighLevelClient client) {
    this.client = client;
  }

  @ExecuteOn(TaskExecutors.IO)
  @Get("/document/sync/{id}")
  public String byId(@PathVariable("id") String documentId) throws IOException {
    var response = client.get(new GetRequest(Constants.INDEX, documentId), RequestOptions.DEFAULT);
    LOG.debug("Response /document/sync/{} => {}", documentId, response.getSourceAsString());
    return response.getSourceAsString();
  }

  @Get("/document/async/{id}")
  public CompletableFuture<String> byIdAsync(@PathVariable("id") String documentId) {
    final CompletableFuture<String> whenDone = new CompletableFuture<>();
    client.getAsync(new GetRequest(Constants.INDEX, documentId), RequestOptions.DEFAULT,
        new ActionListener<>() {
          @Override
          public void onResponse(final GetResponse response) {
            LOG.debug("Response /document/async/{} => {}", documentId, response.getSourceAsString());
            whenDone.complete(response.getSourceAsString());
          }

          @Override
          public void onFailure(final Exception e) {
            whenDone.completeExceptionally(e);
          }
        });
    return whenDone;
  }

  @Get("/document/async/firstname/{search}")
  public CompletableFuture<String> byFirstName(@PathVariable("search") String search) {
    var whenDone = new CompletableFuture<String>();

    final SearchSourceBuilder source = new SearchSourceBuilder()
        .query(QueryBuilders.matchQuery("first_name", search));
    final SubmitAsyncSearchRequest request = new SubmitAsyncSearchRequest(source, Constants.INDEX);
    client.asyncSearch().submitAsync(request,
        RequestOptions.DEFAULT, new ActionListener<>() {
          @Override
          public void onResponse(final AsyncSearchResponse asyncSearchResponse) {
            var hits = asyncSearchResponse.getSearchResponse().getHits().getHits();
            var response = Stream.of(hits).map(SearchHit::getSourceAsString).collect(Collectors.toList());
            LOG.debug("Response /document/async/firstname/{} => {}", search, response);
            whenDone.complete(response.toString());
          }

          @Override
          public void onFailure(final Exception e) {
            whenDone.completeExceptionally(e);
          }
        });
    return whenDone;
  }

}
