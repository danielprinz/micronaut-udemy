FROM openjdk:14-alpine
COPY build/libs/mn-websockets-*-all.jar mn-websockets.jar
EXPOSE 8080
CMD ["java", "-Dcom.sun.management.jmxremote", "-Xmx128m", "-jar", "mn-websockets.jar"]