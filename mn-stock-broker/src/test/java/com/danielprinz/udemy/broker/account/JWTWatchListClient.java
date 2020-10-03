package com.danielprinz.udemy.broker.account;

import java.util.UUID;

import com.danielprinz.udemy.broker.model.WatchList;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Header;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.security.authentication.UsernamePasswordCredentials;
import io.micronaut.security.token.jwt.render.BearerAccessRefreshToken;
import io.reactivex.Flowable;
import io.reactivex.Single;

@Client("/")
public interface JWTWatchListClient {

  @Post("/login")
  BearerAccessRefreshToken login(@Body UsernamePasswordCredentials credentials);

  @Get("/account/watchlist-reactive")
  Flowable<WatchList> retrieveWatchList(@Header String authorization);

  @Get("/account/watchlist-reactive/single")
  Single<WatchList> retrieveWatchListAsSingle(@Header String authorization);

  @Put("/account/watchlist-reactive")
  HttpResponse<Object> updateWatchList(@Header String authorization, @Body WatchList watchList);

  @Delete("/account/watchlist-reactive/{accountId}")
  HttpResponse<Object> deleteWatchList(@Header String authorization, @PathVariable UUID accountId);
}
