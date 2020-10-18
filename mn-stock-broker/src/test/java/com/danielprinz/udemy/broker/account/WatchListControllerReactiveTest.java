package com.danielprinz.udemy.broker.account;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.danielprinz.udemy.broker.model.Symbol;
import com.danielprinz.udemy.broker.model.WatchList;
import com.danielprinz.udemy.broker.store.InMemoryAccountStore;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.runtime.EmbeddedApplication;
import io.micronaut.security.authentication.UsernamePasswordCredentials;
import io.micronaut.security.token.jwt.render.BearerAccessRefreshToken;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.reactivex.Single;

@MicronautTest
public class WatchListControllerReactiveTest {

  private static final Logger LOG = LoggerFactory.getLogger(WatchListControllerReactiveTest.class);
  private static final UUID TEST_ACCOUNT_ID = WatchListControllerReactive.ACCOUNT_ID;

  @Inject
  EmbeddedApplication application;

  @Inject
  @Client("/") JWTWatchListClient client;

  @Inject
  InMemoryAccountStore store;

  @Test
  void returnsEmptyWatchListForAccount() {
    final Single<WatchList> result = client.retrieveWatchList(getAuthorizationHeader()).singleOrError();
    assertTrue(result.blockingGet().getSymbols().isEmpty());
    assertTrue(store.getWatchList(TEST_ACCOUNT_ID).getSymbols().isEmpty());
  }

  @Test
  void returnsWatchListForAccount() {
    final List<Symbol> symbols = Stream.of("APPL", "AMZN", "NFLX")
      .map(Symbol::new)
      .collect(Collectors.toList());
    WatchList watchList = new WatchList(symbols);
    store.updateWatchList(TEST_ACCOUNT_ID, watchList);

    var result = client.retrieveWatchList(getAuthorizationHeader()).singleOrError().blockingGet();
    assertEquals(3, result.getSymbols().size());
    assertEquals(3, store.getWatchList(TEST_ACCOUNT_ID).getSymbols().size());
  }

  @Test
  void returnsWatchListForAccountAsSingle() {
    final List<Symbol> symbols = Stream.of("APPL", "AMZN", "NFLX")
      .map(Symbol::new)
      .collect(Collectors.toList());
    WatchList watchList = new WatchList(symbols);
    store.updateWatchList(TEST_ACCOUNT_ID, watchList);

    final WatchList result = client.retrieveWatchListAsSingle(getAuthorizationHeader()).blockingGet();
    assertEquals(3, result.getSymbols().size());
    assertEquals(3, store.getWatchList(TEST_ACCOUNT_ID).getSymbols().size());
  }

  @Test
  void canUpdateWatchListForAccount() {
    final List<Symbol> symbols = Stream.of("APPL", "AMZN", "NFLX")
      .map(Symbol::new)
      .collect(Collectors.toList());
    WatchList watchList = new WatchList(symbols);

    final HttpResponse<Object> added = client.updateWatchList(getAuthorizationHeader(), watchList);
    assertEquals(HttpStatus.OK, added.getStatus());
    assertEquals(watchList, store.getWatchList(TEST_ACCOUNT_ID));
  }

  @Test
  void canDeleteWatchListForAccount() {
    final List<Symbol> symbols = Stream.of("APPL", "AMZN", "NFLX")
      .map(Symbol::new)
      .collect(Collectors.toList());
    WatchList watchList = new WatchList(symbols);
    store.updateWatchList(TEST_ACCOUNT_ID, watchList);
    assertFalse(store.getWatchList(TEST_ACCOUNT_ID).getSymbols().isEmpty());

    final HttpResponse<Object> deleted = client.deleteWatchList(getAuthorizationHeader(), WatchListControllerReactive.ACCOUNT_ID);
    assertEquals(HttpStatus.OK, deleted.getStatus());
    assertTrue(store.getWatchList(TEST_ACCOUNT_ID).getSymbols().isEmpty());
  }

  private String getAuthorizationHeader() {
    return "Bearer " + givenMyUserLoggedIn().getAccessToken();
  }

  private BearerAccessRefreshToken givenMyUserLoggedIn() {
    return client.login(new UsernamePasswordCredentials("my-user", "secret"));
  }

}
