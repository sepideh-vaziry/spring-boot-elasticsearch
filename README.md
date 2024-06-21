Spring Boot + Elasticsearch
=================
This project demonstrates the integration of Spring Boot with Elasticsearch. 
It provides a basic example of how to set up, configure, and use Elasticsearch in a Spring Boot application.

- [Introduction](#introduction)
- [Features](#features)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Configuration](#configuration)
- [Running the Application](#Running-the-Application)

## Introduction

This sample project showcases the integration between Spring Boot and Elasticsearch, a powerful search and analytics engine. 
The project includes basic CRUD operations for managing and querying data stored in Elasticsearch.

## Features

- Spring Boot application setup
- Elasticsearch configuration
- Basic CRUD operations
- Sample REST API endpoints

## Prerequisites

- Java 17 or higher
- Spring Boot 3.3.0
- Elasticsearch 8.14.1

## Installation
Start a single-node cluster

1. Create a new docker network.
    ```shell
    docker network create elastic-net
    ```

2. Pull the Elasticsearch Docker image.
    ```shell
    docker pull docker.elastic.co/elasticsearch/elasticsearch:8.14.1
    ```

3. Configure the following environment variable.
    ```shell
    export ELASTIC_PASSWORD="<ES_PASSWORD>"  # password for "elastic" username
    ```
   
4. Start the Elasticsearch container with the following command:
   ```shell
   docker run -p 127.0.0.1:9200:9200 -d --name elasticsearch --network elastic-net \
     -e ELASTIC_PASSWORD=$ELASTIC_PASSWORD \
     -e "discovery.type=single-node" \
     -e "xpack.security.http.ssl.enabled=false" \
     docker.elastic.co/elasticsearch/elasticsearch:8.14.1
   ```

## Configuration

Update the `application.properties` file located in `src/main/resources` with your Elasticsearch host and port details:

```properties
spring.elasticsearch.uris=http://localhost:9200
spring.elasticsearch.username=elastic
spring.elasticsearch.password=1234567
```

## Running the Application
Start the Spring Boot application:
```shell
mvn spring-boot:run
```