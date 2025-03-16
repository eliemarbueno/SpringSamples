# SpringBootSamples

This repository I'll Use to include spring boot samples.

### Catalog

CheckList:
Deploy a simple application on AWS Environment.

# Catalog Application

## Description

This is a sample project using Spring Boot to create a catalog application. The application includes endpoints to manage categories and products.
This project created to test environment and review some technologies.
Each branch will include some features or technologies. This strategy make easier to review / learning and compere evolution of tihs sample.

This application started as a simple Rest API that you can access http://localhost:8000 running with a docker.
To do this easily, you can run `docker compose up --build` .

## Prerequisites

- Docker
- Docker Compose
- Java 21
- Maven

## Database

mysql 9.0 -> you can run by command: docker container run --rm -p 3306:3306 --name mysql -e MYSQL_ROOT_PASSWORD=root -d mysql:9.0  --log-bin-trust-function-creators=1

## Configuration

Make sure the required environment variables are set in the `.env` file:

```env
SERVER_PORT=8000
LOCAL_SERVER_PORT=8000
LOCAL_DEBUG_PORT=5005
CONTAINER_DEBUG_PORT=5005
SPRING_PROFILES_ACTIVE=dev
SPRING_DATASOURCE_URL=jdbc:mysql://mysql_db:3306/catalog_db?createDatabaseIfNotExist=true&serverTimeZone=UTC&currentSchema=catalog_db
SPRING_DATASOURCE_USERNAME=catalog_user
SPRING_DATASOURCE_PASSWORD=catalog_password
SPRING_DATASOURCE_DRIVER_CLASS_NAME=com.mysql.cj.jdbc.Driver
SPRING_JPA_HIBERNATE_DIALECT=org.hibernate.dialect.MySQLDialect
SPRING_DATASOURCE_VALIDATION_QUERY=SELECT 1

MYSQL_ROOT_PASSWORD=root_password
MYSQL_DATABASE=catalog_db
MYSQL_USER=catalog_user
MYSQL_PASSWORD=catalog_password
```
