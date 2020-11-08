package com.danielprinz.udemy.auth;

import java.util.Collections;
import java.util.HashMap;
import java.util.Optional;

import javax.annotation.Nullable;
import javax.inject.Singleton;

import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.danielprinz.udemy.auth.persistence.UserEntity;
import com.danielprinz.udemy.auth.persistence.UserRepository;

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
public class JDBCAuthenticationProvider implements AuthenticationProvider {

  private static final Logger LOG = LoggerFactory.getLogger(JDBCAuthenticationProvider.class);
  private final UserRepository users;

  public JDBCAuthenticationProvider(final UserRepository users) {
    this.users = users;
  }

  @Override
  public Publisher<AuthenticationResponse> authenticate(
      @Nullable final HttpRequest<?> httpRequest,
      final AuthenticationRequest<?, ?> authenticationRequest) {
    return Flowable.create(emitter -> {
      final String identity = (String) authenticationRequest.getIdentity();
      LOG.debug("User {} tries to login...", identity);

      final Optional<UserEntity> maybeUser = users.findByEmail(identity);
      if (maybeUser.isPresent()) {
        LOG.debug("Found user: {}", maybeUser.get().getEmail());
        final String secret = (String) authenticationRequest.getSecret();
        if (maybeUser.get().getPassword().equals(secret)) {
          // pass
          LOG.debug("User logged in.");
          final HashMap<String, Object> attributes = new HashMap<>();
          attributes.put("hair_color", "brown");
          attributes.put("language", "en");
          final UserDetails userDetails = new UserDetails(
              identity,
              Collections.singletonList("ROLE_USER"),
              attributes
          );
          emitter.onNext(userDetails);
          emitter.onComplete();
          return;
        } else {
          LOG.debug("Wrong password provided for user {}", identity);
        }
      } else {
        LOG.debug("No user found with email: {}", identity);
      }

      emitter.onError(new AuthenticationException(new AuthenticationFailed("Wrong username or password")));
    }, BackpressureStrategy.ERROR);
  }
}
