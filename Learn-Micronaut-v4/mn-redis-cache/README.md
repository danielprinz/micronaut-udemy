## MN Redis Cache

## Local Redis Stack including RedisInsight as GUI

We will use Docker to spin up a local Redis instance.

1. Ensure to set the `redis.uri: redis://localhost` in the `application.yml` file.
2. Run `docker run -d --name redis-stack -p 6379:6379 -p 8001:8001 redis/redis-stack:6.2.6-v15`
3. Access the GUI via: `http://localhost:8001/`

https://redis.io/docs/latest/operate/oss_and_stack/install/install-stack/docker/

## Headlines Sample Data

```
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
```
