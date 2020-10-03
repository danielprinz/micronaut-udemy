FROM oracle/graalvm-ce:20.2.0-java11 as graalvm
RUN gu install native-image

COPY . /home/app/mn-pricing
WORKDIR /home/app/mn-pricing

RUN native-image --no-server -cp build/libs/mn-pricing-*-all.jar

FROM frolvlad/alpine-glibc
RUN apk update && apk add libstdc++
# No rest api, so no port to expose
# EXPOSE 8080
COPY --from=graalvm /home/app/mn-pricing/mn-pricing /app/mn-pricing
ENTRYPOINT ["/app/mn-pricing"]
