# MN Data MongoDB Async

Showcase the Micronaut Data integration of MongoDB using the reactive-mongodb driver.

## Local MongoDB

We use Micronaut Test resources to spin up a local MongoDB instance. If you want to control it yourself you can also run a MongoDB instance yourself. Ensure to set the mongodb.uri in the application.yml file.

```
docker run -d --name my-mongodb -p 27017:27017 \
-e MONGO_INITDB_DATABASE=jokes \
mongo:7-jammy
 ```

https://hub.docker.com/_/mongo

**Note:** This configuration has authentication disabled! If you use MongoDB in production make sure to configure a username / password.
