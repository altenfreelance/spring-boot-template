# Your new web service

## Prereqs

Java 8 (zulu java)
maven
IDP (OpenID Connect)

## Whats included in the starter template?

* Spring Security (oauth2 resource server integration)

* Profiles: dev, test, qa, prod, test-suite

* Code coverage gate: will fail your build if not up to par. There are example tests here already.

* Automated swagger docs localhost:8080/api/template/swagger-ui/index.html (excluded from prod profile)

* Default logging configuration (logback.xml)

## TODO

* Implement property file encryption using JASYPT if you are needing to commit any secrets
* Add open id connect realm to application.properties (idp.root.url)
* Fill out this readme
* Refactor this template to meet the business requirements for your new web service

## Build

`mvn clean install`

## Run

`mvn clean spring-boot:run -Dspring-boot.run.profiles=dev`

## Documentation

View documentation in http://localhost:8080/api/template/swagger-ui/index.html

## What does this application do?

## How to build to a test/QA environment
