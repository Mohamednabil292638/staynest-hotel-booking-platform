# StayNest — Hotel & Property Booking Platform

[![Java](https://img.shields.io/badge/Java-23-orange?style=for-the-badge&logo=openjdk)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.1-brightgreen?style=for-the-badge&logo=springboot)](https://spring.io/projects/spring-boot)
[![Spring Security](https://img.shields.io/badge/Spring%20Security-JWT-green?style=for-the-badge)](https://spring.io/projects/spring-security)
[![MySQL](https://img.shields.io/badge/MySQL-Database-blue?style=for-the-badge&logo=mysql)](https://www.mysql.com/)
[![Swagger](https://img.shields.io/badge/API-Swagger%2FOpenAPI-85EA2D?style=for-the-badge&logo=swagger&logoColor=black)](https://swagger.io/)
[![Maven](https://img.shields.io/badge/Maven-Build-red?style=for-the-badge&logo=apachemaven)](https://maven.apache.org/)

> A backend-focused hotel and property booking platform built with Java and Spring Boot, covering authentication, hotel and room management, availability search, reservations, dynamic pricing, and REST API documentation.

---

## 📌 Project Overview

**StayNest** is a backend application designed to model the core workflow of a hotel/property booking platform.

The system provides REST APIs for:

- User authentication
- Role-based access control
- Hotel and room management
- Room inventory and availability
- Hotel search by city and date range
- Booking creation and cancellation
- Guest information management
- Dynamic pricing
- Payment-related workflows
- Interactive API documentation using Swagger/OpenAPI

The project is designed as a practical backend engineering project to understand how a real-world booking domain can be structured using Spring Boot, Spring Security, JPA, and relational databases.

---

## 🎯 Business Problem

Hotel booking systems need to manage several connected workflows:

1. Users need to authenticate securely.
2. Hotel managers need to manage properties and rooms.
3. Guests need to search for available rooms.
4. The system needs to verify room inventory for selected dates.
5. Guests need to create and manage reservations.
6. Pricing may change depending on booking conditions.
7. APIs need validation, authorization, and consistent error handling.

StayNest brings these workflows together into a single backend application.

---

## 🚀 Key Features

| Feature | Description |
|---|---|
| 🔐 Authentication | JWT-based authentication |
| 👤 Role-Based Access | Guest and Hotel Manager roles |
| 🏨 Hotel Management | Create, update, activate and manage hotels |
| 🛏️ Room Management | Manage room types and room inventory |
| 🔎 Hotel Search | Search properties using location and date range |
| 📅 Availability | Check room availability for selected dates |
| 📋 Reservations | Initialize and manage hotel bookings |
| ❌ Cancellation | Cancel eligible reservations |
| 💰 Dynamic Pricing | Pricing strategies based on booking conditions |
| 💳 Payment Workflow | Payment-related booking workflow and webhook integration |
| 📖 API Documentation | Interactive Swagger/OpenAPI documentation |
| 🗄️ Database | JPA-based relational database persistence |
| 🧪 Testing | Spring Boot testing support |

---

## 👥 User Roles

### Guest

A guest can:

- Register/login
- Search hotels
- Check room availability
- View room information
- Start a booking
- Provide guest details
- Complete the booking/payment workflow
- Cancel eligible bookings

### Hotel Manager

A hotel manager can:

- Authenticate through the manager role
- Manage hotel properties
- Manage room information
- View hotel-related data
- Perform administrative hotel operations

---

## 🔄 Booking Workflow

The main booking flow is designed around the following process:

```text
User
  ↓
Authentication
  ↓
Search Hotels
  ↓
Select Dates
  ↓
Check Room Availability
  ↓
Select Room
  ↓
Initialize Booking
  ↓
Add Guest Details
  ↓
Payment Workflow
  ↓
Booking Confirmation


Cancellation follows a separate controlled workflow based on the booking state.
🏗️ Architecture
The application follows a layered backend architecture:
Client
   │
   ▼
REST Controllers
   │
   ▼
Service Layer
   │
   ▼
Repository Layer
   │
   ▼
JPA / Hibernate
   │
   ▼
Relational Database

Main Layers
Controller Layer
Handles HTTP requests and exposes REST APIs.
Service Layer
Contains business logic such as:
- Hotel operations
- Room availability
- Booking workflows
- Pricing calculations
- Authentication-related operations
Repository Layer
Handles database access through Spring Data JPA.
Entity Layer
Represents the application's core domain objects and database relationships.
Security Layer
Handles authentication, authorization and JWT-based access control.
🔐 Security
The application uses Spring Security with JWT-based authentication.
The general authentication flow is:
Login Request
     ↓
Authentication
     ↓
JWT Access Token
     ↓
Authorization Header
     ↓
Protected API
     ↓
Role Validation

Role-based authorization is used to restrict administrative operations.
🗄️ Database
The application uses JPA/Hibernate for persistence.
Supported database configurations include:
- H2 for demonstration/development
- MySQL for persistent database environments
The domain is centered around entities such as:
User
Hotel
Room
Room Inventory
Booking
Guest Details
Payment

Relationships between these entities support hotel discovery, room availability and reservation workflows.
💰 Dynamic Pricing
The application includes pricing strategies that can adjust room prices according to booking conditions.
Pricing factors include concepts such as:
- Surge pricing
- Holiday pricing
- Booking urgency
- Occupancy
This demonstrates how business rules can be separated from the basic booking workflow.
📖 API Documentation
The backend provides interactive API documentation through Swagger/OpenAPI.
After starting the application locally:
http://localhost:8080/api/v1/swagger-ui/index.html

Swagger can be used to:
- Explore available endpoints
- View request/response models
- Test REST APIs
- Understand API contracts
- Test authenticated endpoints
🛠️ Technology Stack
Backend
- Java 23
- Spring Boot 4
- Spring Web MVC
- Spring Security
- Spring Data JPA
- Hibernate
Database
- MySQL
- H2
API
- REST APIs
- Swagger/OpenAPI
- JSON
Authentication
- JWT
- Spring Security
Build & Development
- Maven
- Git
- GitHub
- IntelliJ IDEA / VS Code
- Postman / Swagger UI
Payment
- Stripe integration/workflow
📂 Project Structure
src/
├── main/
│   ├── java/
│   │   └── ...
│   │
│   └── resources/
│       ├── application.properties
│       ├── application-prod.properties
│       └── ...
│
└── test/
    └── java/
        └── ...

The source code is organized around the application's domain, controllers, services, repositories, entities, security and configuration.
⚙️ Run Locally
Requirements
- JDK 23+
- Maven 3.9+
- MySQL (when using persistent MySQL configuration)
- Git
Clone
git clone https://github.com/Mohamednabil292638/staynest-hotel-booking-platform.git

Navigate to the project
cd staynest-hotel-booking-platform

Configure environment variables
Create a local .env file based on:
.env.example

Configure the required database and authentication values.
Run
mvn spring-boot:run

Open Swagger
http://localhost:8080/api/v1/swagger-ui/index.html

🧪 Testing
Run the automated tests using:
mvn test

Testing is used to verify application behavior and reduce regressions while modifying the backend.
🔍 What I Worked On
As part of my backend engineering learning and portfolio development, I worked with this project to gain practical experience in:
- Java backend development
- Spring Boot application structure
- REST API development
- Spring Security and JWT authentication
- Role-based authorization
- JPA/Hibernate persistence
- Relational database integration
- Booking-domain business workflows
- API testing and debugging
- Swagger/OpenAPI documentation
- Maven-based project management
- Git and GitHub workflow
Further modifications and feature improvements will be documented as they are implemented and verified.
📈 Future Improvements
Planned improvements include:
- Automated integration test coverage
- Improved API validation and error responses
- Production database configuration
- Containerized deployment
- CI/CD pipeline
- Monitoring and logging
- Improved booking concurrency handling
- Additional automated tests for pricing and availability
👨‍💻 Developer
Mohamed Nabil S
B.Tech — Artificial Intelligence & Data Science
Bengaluru, India
GitHub:
https://github.com/Mohamednabil292638
LinkedIn:
https://www.linkedin.com/in/mohamed-nabil-s-950104308/
📌 Project Status
Status: Active Development
The project is being studied, configured, tested and extended as part of my Java/Spring Boot backend engineering portfolio.
📄 Attribution & License
Original project attribution and applicable license information are retained in the repository.
This repository is being used as a learning and portfolio project, with modifications and extensions documented as they are implemented.

### One correction before you paste it

I deliberately **didn't put “Built from scratch by Mohamed Nabil S”** or claim that every feature is your original implementation.

That's important because your goal is eventually to put this project on your resume and discuss it confidently in interviews. As we actually **modify, debug, test, and extend the application**, we can progressively replace the generic section with your **real contributions**.

Also, don't add a fake live-demo link yet. Once **we actually deploy your version**, we'll add:

```text
🌐 Live Demo
📖 Swagger API
