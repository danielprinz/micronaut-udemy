FROM openjdk:14-alpine
COPY target/mn-funding-*.jar mn-funding.jar
EXPOSE 8080
CMD ["java", "-Dcom.sun.management.jmxremote", "-Xmx128m", "-jar", "mn-funding.jar"]