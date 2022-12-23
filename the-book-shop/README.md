# Software Testing Lab

[![example workflow](https://github.com/ShathaCodes/SoftwareTestingLab/actions/workflows/maven.yml/badge.svg)](https://github.com/ShathaCodes/SoftwareTestingLab/actions/workflows/maven.yml)

The Book Shop is an application that manages books!

The project has a basic Frontend/Backend application with basic CRUD features. The main focus is to apply all different test levels as well as create a CI/CD pipeline for the project.

## DevOps Lab

As part of The DevOps lab, I built a CI/Cd pipeline using Github actions.

### 1. Build and Test

1.  Build and Test the backend with maven and save the generated jar file to use it later.
2.  Build the Frontend with maven as well and save the generated jar file to use it later.

### 2. Package

Package both the frontend and the backend seperatly into two docker images using the generated jar files and push them to DockerHub.

The images are tagged with The commit SHA. 

### 3. End to End Tests

Create a ``.env`` file with the commit SHA which will be used by the ``docker-compose.yml`` file to pull the newly pushed Docker images.

Build the cluster with ``docker-compose`` inside the Runner.

Run the Cypress tests and save the generated video file as an artifact.

### 4. Deploy

Ssh into the EC2 instance with a secret key. The EC2 instance has docker and docker-compose installed and its security group has an inbound rule that exposes port 9000. 

Create a ``.env`` file with the commit SHA and the MySQL parameters which will be used by the ``docker-compose.yml`` file.

Pull from the git repository to ensure the ``docker-compose`` file is updated.

Build the cluster with ``docker-compose``.

**Results**

You can access the website [here](http://3.8.237.227:9000/books)

![exec](https://raw.githubusercontent.com/ShathaCodes/SoftwareTestingLab/main/results.PNG)

## Software Testing Lab

As part of The Software Testing Lab, We will be performing four levels of tests :

1. Unit Tests
2. Integration Tests
3. System Tests
4. Acceptance Tests

You can view the status of the tests in the pipeline or run the tests manually.

### 1. Unit Tests

In this step, I focused on testing the different actions (functions) in the BookController in the **Backend**.

I used Mockito to mock the calls for the book repository that is used in the Unit Tests. That way, we can make sure we are trully isolating the functions and testing only the functionalities of the controller's actions.

You can run the test manually with :
```
cd Back
mvn test
```

### 2. Integration Tests

Here we test the integration of different parts of the Backend.

I will be using a seperate database for testing so that it won't affect the actual Database. 

For that, I used the **Testcontainers** Library. This Java library is used to create a lightweight, throwaway instance of MySQL database using a Docker image. The container can be controlled with Java code which is very convenient.

You can run the test manually with :
```
cd Back
mvn test
```

### 3. System Tests

I used Cypress to do an End-to-End test for the whole application.

I have 3 different scenarios:

1.  Create a new book
2.  Edit a book
3.  Delete a book

You can run the test manually with :
```
cd e2e-tests
npm run cypress:run
```

### 4. Acceptance Tests

I used a User Acceptance Test Template to test two features : 

- Adding a new Book
- Reducing Book Stock

## Observability

In order to fully monitor the application, you can use this [repo](https://github.com/ShathaCodes/observability-helm-charts) which sets up a monitoring system quickly and easily.

### 1. Logging
I used **Simple Logging Facade for Java (SLF4J)** to enable logging in my application. I also enabled **DispatcherServlet** logging.

### 2. Metrics
I used **Spring Boot Actuator** which exposes metrics to be pulled by Prometheus. 
Behind the hoods, Spring Boot Actuator uses Micrometer to instrument and capture different metrics from the code, such as: JVM Memory usage, CPU usage, Connection Pool information, HTTP requests and so on.

### 3. Traces
I used **Spring Cloud Sleuth** which provides Spring Boot auto-configuration for distributed tracing.
I added **spring-cloud-sleuth-zipkin** so that the app will generate and report Zipkin-compatible traces via HTTP. 

