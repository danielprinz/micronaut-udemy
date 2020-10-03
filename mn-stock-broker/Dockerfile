FROM openjdk:14-alpine
COPY target/mn-stock-broker-*.jar mn-stock-broker.jar
EXPOSE 8080
CMD ["java", "-Dcom.sun.management.jmxremote", "-Xmx128m", "-jar", "mn-stock-broker.jar"]