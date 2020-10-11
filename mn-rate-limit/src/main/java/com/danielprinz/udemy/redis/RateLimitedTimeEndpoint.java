package com.danielprinz.udemy.redis;

import java.time.Clock;
import java.time.LocalTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;

@Controller("/time")
public class RateLimitedTimeEndpoint {

  private static final Logger LOG = LoggerFactory.getLogger(RateLimitedTimeEndpoint.class);
  private static final int QUOTA_PER_MINUTE = 10;
  private final StatefulRedisConnection<String, String> redis;

  public RateLimitedTimeEndpoint(final StatefulRedisConnection<String, String> redis) {
    this.redis = redis;
  }

  @ExecuteOn(TaskExecutors.IO)
  @Get("/")
  public String time() {
    return getTime("EXAMPLE::TIME", LocalTime.now());
  }

  @ExecuteOn(TaskExecutors.IO)
  @Get("/utc")
  public String utc() {
    return getTime("EXAMPLE::UTC", LocalTime.now(Clock.systemUTC()));
  }

  private String getTime(final String key, final LocalTime now) {
    final String value = redis.sync().get(key);
    int currentQuota = null == value ? 0 : Integer.parseInt(value);
    if (currentQuota >= QUOTA_PER_MINUTE) {
      final String err = String.format("Rate limit reached %s %s/%s", key, currentQuota, QUOTA_PER_MINUTE);
      LOG.info(err);
      return err;
    }
    LOG.info("Current quota {} in {}/{}", key, currentQuota, QUOTA_PER_MINUTE);
    increaseCurrentQuota(key);
    return now.toString();
  }

  private void increaseCurrentQuota(final String key) {
    final RedisCommands<String, String> commands = redis.sync();
    commands.multi();
    commands.incrby(key, 1);
    var remainingSeconds = 60 - LocalTime.now().getSecond();
    commands.expire(key, remainingSeconds);
    commands.exec();
  }

}
