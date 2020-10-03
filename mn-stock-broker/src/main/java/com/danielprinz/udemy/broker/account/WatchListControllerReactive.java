package com.danielprinz.udemy.broker.account;

import java.util.UUID;
import java.util.concurrent.ExecutorService;

import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.danielprinz.udemy.broker.model.WatchList;
import com.danielprinz.udemy.broker.store.InMemoryAccountStore;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Put;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller("/account/watchlist-reactive")
public class WatchListControllerReactive {

  private static final Logger LOG = LoggerFactory.getLogger(WatchListControllerReactive.class);
  static final UUID ACCOUNT_ID = UUID.randomUUID();

  private final InMemoryAccountStore store;
  private final Scheduler scheduler;

  public WatchListControllerReactive(
    @Named(TaskExecutors.IO) ExecutorService executorService,
    final InMemoryAccountStore store) {
    this.store = store;
    this.scheduler = Schedulers.from(executorService);
  }

  @Get(produces = MediaType.APPLICATION_JSON)
  @ExecuteOn(TaskExecutors.IO)
  public WatchList get() {
    LOG.debug("getWatchlist - {}", Thread.currentThread().getName());
    return store.getWatchList(ACCOUNT_ID);
  }

  @Get(
    value = "/single",
    produces = MediaType.APPLICATION_JSON
  )
  public Single<WatchList> getAsSingle() {
    return Single.fromCallable(() -> {
      LOG.debug("getAsSingle - {}", Thread.currentThread().getName());
      return store.getWatchList(ACCOUNT_ID);
    }).subscribeOn(scheduler);
  }

  @Put(
    consumes = MediaType.APPLICATION_JSON,
    produces = MediaType.APPLICATION_JSON
  )
  @ExecuteOn(TaskExecutors.IO)
  public WatchList update(@Body WatchList watchList) {
    return store.updateWatchList(ACCOUNT_ID, watchList);
  }

  @Delete(
    value = "/{accountId}",
    consumes = MediaType.APPLICATION_JSON,
    produces = MediaType.APPLICATION_JSON
  )
  @ExecuteOn(TaskExecutors.IO)
  public void delete(@PathVariable UUID accountId) {
    store.deleteWatchList(accountId);
  }
}
