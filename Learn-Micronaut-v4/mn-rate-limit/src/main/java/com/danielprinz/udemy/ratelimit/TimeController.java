package com.danielprinz.udemy.ratelimit;

import io.lettuce.core.api.StatefulRedisConnection;
import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import java.time.Clock;
import java.time.LocalTime;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller("/time")
@ExecuteOn(TaskExecutors.BLOCKING)
public class TimeController {

  private static final Logger LOG = LoggerFactory.getLogger(TimeController.class);
  private static final int QUOTA_PER_MINUTE = 10;
  private final StatefulRedisConnection<String, String> redis;

  public TimeController(StatefulRedisConnection<String, String> redis) {
    this.redis = redis;
  }

  @Get()
  public HttpResponse<?> time(HttpHeaders headers) {
    var clientId = Optional.ofNullable(headers.get("x-client-id")).orElse("ANONYMOUS");
    final var key = "TIMECONTROLLER::TIME::" + clientId;
    return checkQuotaAndGetTime(key, LocalTime.now());
  }

  @Get("/utc")
  public HttpResponse<?> timeUtc() {
    final var key = "TIMECONTROLLER::TIMEUTC";
    return checkQuotaAndGetTime(key, LocalTime.now(Clock.systemUTC()));
  }

  private HttpResponse<?> checkQuotaAndGetTime(String key, LocalTime localTime) {
    var redisQuota = Optional.ofNullable(redis.sync().get(key))
      .orElse("0");
    if (Integer.parseInt(redisQuota) >= QUOTA_PER_MINUTE) {
      var err = String.format("Rate limit reached %s %s/%s", key, redisQuota, QUOTA_PER_MINUTE);
      LOG.info(err);
      return HttpResponse.status(HttpStatus.TOO_MANY_REQUESTS, err);
    }
    LOG.info("Current quota of {} is {}/{}", key, redisQuota, QUOTA_PER_MINUTE);
    increaseQuota(key);

    return HttpResponse.ok(localTime.toString());
  }

  private void increaseQuota(final String redisKey) {
    var commands = redis.sync();
    commands.multi();
    commands.incrby(redisKey, 1);
    var remainingSeconds = 60 - LocalTime.now().getSecond();
    commands.expire(redisKey, remainingSeconds);
    commands.exec();
  }
}
