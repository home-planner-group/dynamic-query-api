# Dynamic Query API

This project was generated with [Quarkus Initializer](https://code.quarkus.io/) version 2.7.

The application starts on [http://localhost:8088](http://localhost:8088).

### Purpose

This project has the purpose to get involved with __Quarkus__ (Java, Maven), __Docker__ and __GitHub__ (Actions,
Packages, Projects).

### Description

...

## Architecture

### Overview

```
         Http Entrypoint 
                |           
Models  --> Controller --> ExceptionHandler
                |
Utility --> Database
                |
            MySQL DBMS
```

### Explanation

* Http Entrypoint = `localhost:8088/request-path`
* Controller = [controller-package](src/main/java/com/planner/api/controller)
* Models = [model-package](src/main/java/com/planner/api/model)
* ExceptionHandler = [exception-package](src/main/java/com/planner/api/exception)
* Database = [database-package](src/main/java/com/planner/api/database)
* Utility = [utility-package](src/main/java/com/planner/api/utility)
* MySQL DBMS = `mysql://localhost:3306` or custom

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
    * Build Image: `docker build -t dynamic-query-api -f ./src/main/docker/Dockerfile.jvm .`

## Quarkus Commands

### Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
mvn compile quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.

### Packaging and running the application

The application can be packaged using:
```shell script
mvn package
```
It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:
```shell script
mvn package -Dquarkus.package.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

### Creating a native executable

You can create a native executable using: 
```shell script
mvn package -Pnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: 
```shell script
mvn package -Pnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/dynamic-query-api-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/maven-tooling.

### Related Guides

- RESTEasy JAX-RS ([guide](https://quarkus.io/guides/rest-json)): REST endpoint framework implementing JAX-RS and more
