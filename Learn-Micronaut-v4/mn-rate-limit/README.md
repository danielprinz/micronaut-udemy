## MN Rate Limit

Showcase Redis Rate Limit: https://redis.io/glossary/rate-limiting/

## Local Redis

We use Micronaut Test resources to spin up a local Redis instance. If you want to control it yourself you can also run a
Redis instance yourself.
Ensure to set the `redis.uri` in the `application.yml` file.

`docker run --name my-redis -p 6379:6379 redis:7-alpine`

https://hub.docker.com/_/redis
