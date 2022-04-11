FROM quay.io/quarkus/ubi-quarkus-native-image:21.3.1-java11 AS builder

COPY --chown=quarkus:quarkus mvnw /code/mvnw
COPY --chown=quarkus:quarkus .mvn /code/.mvn
COPY --chown=quarkus:quarkus pom.xml /code/

USER quarkus

WORKDIR /code
RUN chmod +x mvnw
RUN ./mvnw -B org.apache.maven.plugins:maven-dependency-plugin:3.1.2:go-offline
COPY src /code/src
RUN ./mvnw package -Pnative
RUN echo "======== NATIVE TESTS ========"
RUN ./mvnw verify -Pnative

FROM quay.io/quarkus/quarkus-micro-image:1.0 AS runner

WORKDIR /work/
COPY --from=builder /code/target/*-runner /work/application

# set up permissions for user `1001`
RUN chmod 775 /work /work/application \
  && chown -R 1001 /work \
  && chmod -R "g+rwX" /work \
  && chown -R 1001:root /work

EXPOSE 8088
USER 1001

CMD ["./application", "-Dquarkus.http.host=0.0.0.0"]
