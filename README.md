[![Continuous-Integration](https://github.com/home-planner-group/dynamic-query-api/actions/workflows/ci.yml/badge.svg)](https://github.com/home-planner-group/dynamic-query-api/actions/workflows/ci.yml)
[![Docker-Image](https://github.com/home-planner-group/dynamic-query-api/actions/workflows/docker-image.yml/badge.svg)](https://github.com/home-planner-group/dynamic-query-api/actions/workflows/docker-image.yml)

# Dynamic Query API

This project was generated with [Quarkus Initializer](https://code.quarkus.io/) version 2.7.

The application starts on [http://localhost:8088](http://localhost:8088/swagger-ui).

### Description

This project has the purpose to get involved with __Quarkus__ (Java, Maven), __Docker__ and __GitHub__ (Actions,
Packages, Projects).

## Docker Image

* __Dockerfile__: [Multistage Dockerfile for native Build](src/main/docker/Dockerfile)
* __Guide__: [Multistage Native Build](https://quarkus.io/guides/building-native-image#multistage-docker)
* __Default Settings:__ [prod.properties](src/main/resources/application.properties)
  * Exposes `Port 8088`
  * Uses by default __MySQL DB__ at `Port 3306`
* __Pull Image:__
  * `docker pull ghcr.io/home-planner-group/dynamic-query-api:latest`
* __Run Container:__
  * `docker run -d -p 8088:8088 --name query-api ghcr.io/home-planner-group/dynamic-query-api:latest`

## Architecture

```
         Http Entrypoint 
                |           
Models  --> Controller --> ExceptionHandler
                |
Utility --> Database
                |
            MySQL DBMS
```

<details>
  <summary>Explanation</summary>

* Http Entrypoint = `localhost:8088/request-path`
* Controller = [controller-package](src/main/java/com/planner/api/controller)
* Models = [model-package](src/main/java/com/planner/api/model)
* ExceptionHandler = [exception-package](src/main/java/com/planner/api/exception)
* Database = [database-package](src/main/java/com/planner/api/database)
* Utility = [files-package](src/main/java/com/planner/api/files)
* MySQL DBMS = `mysql://localhost:3306` or custom

</details>

## Dev Requirements

* Download and Install [Java Development Kit](https://www.oracle.com/java/technologies/downloads/#jdk17) v17.0+
  * Add `JAVA_HOME`
  * Update `PATH`
* Download and Install [Maven](https://maven.apache.org/download.cgi) v3.8.4+
  * Add `MAVEN_HOME`
  * Update `PATH`
* Download and Install [MySQL Server](https://dev.mysql.com/downloads/installer/) v8.0+
  * Check [application.properties](src/main/resources/application.properties) for correct configuration
* Download and Install [Docker](https://docs.docker.com/desktop/windows/install/)
  * Build Image: `docker build -t dynamic-query-api -f ./src/main/docker/Dockerfile .`

## GitHub Workflows

<details>
  <summary>Continuous Integration Workflow</summary>

* [.github/workflows/ci.yml](.github/workflows/ci.yml)
  * __Trigger:__ all pushes
  * Executes `mvn install`
  * Run `mvn test` on JVM build with MySQL DB
  * Perform __CodeQL__ Analysis with Java

</details>

<details>
  <summary>Docker Image Delivery Workflow</summary>

* [.github/workflows/docker-image.yml](.github/workflows/docker-image.yml)
  * __Trigger:__ manual or on published release
  * Executes `docker build` with native Integration Test
  * Execute `docker push` to GitHub Packages

</details>

## Quarkus Commands

<details>
  <summary>Running the application in dev mode</summary>

You can run your application in dev mode that enables live coding using:

  ```shell script
  ./mvnw compile quarkus:dev
  ```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.
</details>

<details>
  <summary>Packaging and running the application</summary>

The application can be packaged using:

  ```shell script
  ./mvnw package
  ```

It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory. Be aware that it???s not an _??ber-jar_ as
the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _??ber-jar_, execute the following command:

  ```shell script
  ./mvnw package -Dquarkus.package.type=uber-jar
  ```

The application, packaged as an _??ber-jar_, is now runnable using `java -jar target/*-runner.jar`.
</details>

<details>
  <summary>Creating a native executable</summary>

You can create a native executable using:

  ```shell script
  ./mvnw package -Pnative
  ```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using:

  ```shell script
  ./mvnw package -Pnative -Dquarkus.native.container-build=true
  ```

You can then execute your native executable with: `./target/dynamic-query-api-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/maven-tooling.
</details>

<details>
  <summary>Related Guides</summary>

RESTEasy JAX-RS ([guide](https://quarkus.io/guides/rest-json)): REST endpoint framework implementing JAX-RS and more
</details>
