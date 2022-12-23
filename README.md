# DevOps Lab

This project is part of GL5 DevOps Lab. We are meant to create a distributed application, then use it with other tools in order to understand different DevOps concepts.

**The Book Shop** is an application that manages a book inventory. It's a Frontend/Backend application with basic CRUD features, developed using **Spring Boot** and it interrogates a **PostgreSQL** database.

# Observability

In this part, we will focus on the three pillars of Observability:

## 1. Logging
I used **Apache Log4j** to enable logging in my application. 

I made sure to add the ``request_id`` and the ``ip_address`` in every log so that it can help us while debugging.

## 2. Metrics
I used **Spring Boot Actuator** which exposes metrics to be pulled by Prometheus. 

Behind the hoods, Spring Boot Actuator uses Micrometer to instrument and capture different metrics from the code, such as: JVM Memory usage, CPU usage, Connection Pool information, HTTP requests and so on.

I also added a custom metric which is ``low.inventory.count``. Basically after each creation/update of a book in The Book Shop, a query is run to check for books with inventory count lower than 3. The resulting books are shown in a **dimensional Gauge** with 2 different tags : the book id and the book title so that way we can track and restock those books.

## 3. Traces
I used **Spring Cloud Sleuth** which provides Spring Boot auto-configuration for distributed tracing.

I added **spring-cloud-sleuth-zipkin** so that the app will generate and report Zipkin-compatible traces via HTTP. 

The ``span_id`` and the ``trace_id`` are shown in each logs for both Backend and Frontend.


# Automation

# Deployment