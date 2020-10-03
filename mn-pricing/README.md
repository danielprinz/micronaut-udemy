## MN Pricing

Features:
* Kafka Produce / Consume
* GraalVM

#### Graal
1. `./gradlew clean assemble # or ./mvnw clean package if using Maven`
2. `docker build . -t mn-pricing`

As Kafka runs on the host machine the stack must be attached to the correct host network, so the access to kafka works.
3. `docker stack deploy -c app.stack.yml mn-pricing-stack`
