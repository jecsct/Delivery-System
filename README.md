# Order Management System

This is a multi-service Spring Boot project designed as a learning playground for exploring microservices architecture, Kafka, REST APIs, Postman testing, and documentation best practices.

## 🧠 Purpose

This project was built to help me:

- Understand how to structure a Spring Boot project using multiple services
- Learn how to use Kafka for asynchronous communication between services
- Practice writing clean, maintainable, and well-documented code
- Get hands-on experience with API testing using Postman
- Apply best practices in Java, Spring, and software documentation

## 🧱 Architecture

The system is made up of three main services:

- **Order Service:** Manages customer orders using **PostgreSQL** for data persistence.
- **Payment Service:** Handles payment processing for orders using **MongoDB** to store transaction data.
- **Shipping Service:** Responsible for shipping orders once payment is confirmed, using **MongoDB**.

Each service is a separate Spring Boot application and communicates via Kafka topics.

## 📦 Technologies

- **Java 17**
- **Spring Boot**
- **Spring Web / Spring Data JPA** (for Order Service with PostgreSQL)
- **Spring Data MongoDB** (for Payment and Shipping Services with MongoDB)
- **Apache Kafka**
- **PostgreSQL** (for Order Service)
- **MongoDB** (for Payment and Shipping Services)
- **Lombok**
- **Postman** (for testing)
- **Swagger / OpenAPI** (for documentation)
- **Docker** (optional, for future containerization)

## 🔄 Kafka Topics

| Topic Name     | Description                        |
|----------------|------------------------------------|
| `orders`       | Sent when a new order is created   |
| `payments`     | Sent when a payment is processed   |
| `shipping`     | Sent when an order is ready to ship|

## 🗄 Databases

- **PostgreSQL** for the Order Service to store orders data.
- **MongoDB** for Payment and Shipping Services to store transaction and shipping details.

## 🚀 How to Run

Each service is independent. Start them in this order:

1. **Start Kafka, Zookeeper, PostgreSQL, and MongoDB** (using Docker or local install)
2. **Run Order Service** (with PostgreSQL)
3. **Run Payment Service** (with MongoDB)
4. **Run Shipping Service** (with MongoDB)

Each service exposes its own REST API documented with Swagger (accessible at `/swagger-ui.html`).

## 🧪 Testing the APIs

Use the provided Postman collection in `/postman` directory to test creating orders, processing payments, and shipping items.

## 📝 Future Enhancements

- Add Docker support for each service
- Add unit/integration tests
- Integrate service discovery (Eureka/Consul)
- Use a message schema registry (e.g., Avro + Confluent)
- Add authentication/authorization

## 📁 Folder Structure (example)

