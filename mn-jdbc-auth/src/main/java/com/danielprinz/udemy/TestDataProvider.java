package com.danielprinz.udemy;

import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.danielprinz.udemy.auth.persistence.UserEntity;
import com.danielprinz.udemy.auth.persistence.UserRepository;

import io.micronaut.context.event.StartupEvent;
import io.micronaut.runtime.event.annotation.EventListener;

@Singleton
public class TestDataProvider {

  private static final Logger LOG = LoggerFactory.getLogger(TestDataProvider.class);
  private final UserRepository users;

  public TestDataProvider(final UserRepository users) {
    this.users = users;
  }

  @EventListener
  public void init(StartupEvent event) {
    final String email = "alice@example.com";
    if (users.findByEmail(email).isEmpty()) {
      final UserEntity alice = new UserEntity();
      alice.setEmail(email);
      alice.setPassword("secret");
      users.save(alice);
      LOG.debug("Insert user {}", email);
    }
  }
}
