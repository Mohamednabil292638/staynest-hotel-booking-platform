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
