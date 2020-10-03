# Dockerized Postgres

https://hub.docker.com/_/postgres

## Ephemeral Postgres instances
This is the quickest way to get started:
```
docker run --name my-postgres -e POSTGRES_PASSWORD=secret -e POSTGRES_DB=mn-stock-broker -p 5432:5432 -d postgres:12.4
```

* User: postgres
* Password: secret
* Database: mn-stock-broker

## Docker Compose
Execute from root directory:
```
docker-compose -f ./postgres/stack.yml up
```

## Docker Swarm
Execute from root directory:
```
docker stack deploy -c ./postgres/stack.yml postgres
```

Contains a volume for permanent storage of data. On system restart the data is available again.
