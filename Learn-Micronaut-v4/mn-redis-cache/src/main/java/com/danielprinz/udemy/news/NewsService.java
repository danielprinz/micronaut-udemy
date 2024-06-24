package com.danielprinz.udemy.news;

import io.micronaut.cache.annotation.CacheConfig;
import io.micronaut.cache.annotation.CacheInvalidate;
import io.micronaut.cache.annotation.CachePut;
import io.micronaut.cache.annotation.Cacheable;
import jakarta.inject.Singleton;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
@CacheConfig("headlines")
public class NewsService {

  private static final Logger LOG = LoggerFactory.getLogger(NewsService.class);

  private final HashMap<Month, List<String>> headlines = new HashMap<Month, List<String>>() {{
    put(Month.FEBRUARY, Arrays.asList(
      "Micronaut AOP: Awesome flexibility without the complexity",
      "Follow the Micronaut Framework in Mastodon"
    ));
    put(Month.APRIL, Collections.singletonList(
      "Micronaut Framework 4.4.0 released!"
    ));
    put(Month.JUNE, Arrays.asList(
      "Apple WWDC 2024 keynote: iOS 18, AI and changes to photos among what's coming",
      "UEFA EURO 2024 is starting: Full football match schedule")
    );
  }};

  @Cacheable
  public List<String> headlines(Month month) {
    LOG.debug("Simulating delay. Fetch headline with 3s delay...");
    try {
      TimeUnit.SECONDS.sleep(3);
      return headlines.get(month);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  @CachePut(parameters = {"month"})
  public List<String> addHeadlineToCache(Month month, String headline) {
    if (headlines.containsKey(month)) {
      var newList = new ArrayList<>(headlines.get(month));
      newList.add(headline);
      headlines.put(month, newList);
    } else {
      headlines.put(month, Collections.singletonList(headline));
    }
    return headlines.get(month);
  }

  @CacheInvalidate(parameters = {"month"})
  public void removeHeadlineFromCache(Month month, String headline) {
    if (headlines.containsKey(month)) {
      var headlinesPerMonth = new ArrayList<>(headlines.get(month));
      headlinesPerMonth.remove(headline);
      headlines.put(month, headlinesPerMonth);
    }
  }

}
