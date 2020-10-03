package com.danielprinz.udemy.auth.jwt;

import java.util.ArrayList;

import javax.annotation.Nullable;
import javax.inject.Singleton;

import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.micronaut.http.HttpRequest;
import io.micronaut.security.authentication.AuthenticationException;
import io.micronaut.security.authentication.AuthenticationFailed;
import io.micronaut.security.authentication.AuthenticationProvider;
import io.micronaut.security.authentication.AuthenticationRequest;
import io.micronaut.security.authentication.AuthenticationResponse;
import io.micronaut.security.authentication.UserDetails;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;

@Singleton
public class AuthenticationProviderUserPassword implements AuthenticationProvider {

  private final static Logger LOG = LoggerFactory.getLogger(AuthenticationProviderUserPassword.class);

  @Override
  public Publisher<AuthenticationResponse> authenticate(
    @Nullable final HttpRequest<?> httpRequest,
    final AuthenticationRequest<?, ?> authenticationRequest) {
    return Flowable.create(emitter -> {
      final Object identity = authenticationRequest.getIdentity();
      final Object secret = authenticationRequest.getSecret();
      LOG.debug("User {} tries to login...", identity);

      if (identity.equals("my-user") && secret.equals("secret")) {
        // pass
        LOG.debug("User logged in.");
        emitter.onNext(new UserDetails((String) identity, new ArrayList<>()));
        emitter.onComplete();
        return;
      }
      emitter.onError(new AuthenticationException(new AuthenticationFailed("Wrong username or password!")));
    }, BackpressureStrategy.ERROR);
  }
}
