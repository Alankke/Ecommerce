# E-commerce API
This project is an e-commerce API built with Java and Spring Boot, developed to meet the requirements of a Java backend bootcamp by [Globant](https://www.globant.com/). It covers core backend development principles such as REST API development, design patterns, testing, messaging, caching, and cloud deployment.

## Table of Contents
- [Features](#features).
- [Technologies Used](#technologies-used).
- [Try it out](#Try-it-out).

## Features
- CRUD operations for managing e-commerce products, carts, and customers.
- In-memory caching for frequently accessed data.
- Email messaging between with RabbitMQ.
- Dockerized for easy deployment.
- Deployed on AWS EC2.

## Technologies Used
This project was built using the following tools and frameworks:

- **Java 17**.
- Spring Boot: For building the REST API.
- Maven: For project management and dependencies.
- **Lombok**: For reducing boilerplate code.
- JPA & Hibernate: For ORM and database management.
- H2 Database: In-memory database.
- JUnit & Mockito: For unit and integration testing.
- RabbitMQ: For asynchronous email messaging.
- Caching: For improving performance with frequently accessed data.
- Docker: For application containerization.
- AWS EC2: For cloud deployment.

## Design Principles
The application was built following key backend design and development principles, including:

1. **Design and Development Principles**: Adherence to clean code practices and use of design patterns.
2. **Factory and Decorator Patterns**: Used to manage object creation and enhance object functionality, respectively.
3. **Persistence Layer Design: Implemented using JPA and Hibernate for seamless database interaction.
4. **Caching Mechanisms: Improves application performance with Spring caching abstractions.
5. **RESTful API Design: Built with Spring Boot to provide RESTful endpoints for all resources.

## Try it out
You can try the API and see the endpoints by going here [link](http://98.83.153.240/swagger-ui/index.html).
