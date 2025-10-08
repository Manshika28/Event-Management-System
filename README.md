# 🎟️ Event Management System (Microservices-Based)

## 📘 Overview
The **Event Management System** is a microservices-based application designed to simplify event creation, registration, and ticket booking.  
It demonstrates modern distributed system design using **Spring Boot**, **Spring Cloud**, **MySQL**, and **RESTful APIs**.

This project follows a **microservices architecture**, with separate services for event management, user management, and an API gateway to route requests.

---

## 🏗️ Architecture

### Microservices:
- **API Gateway** – Routes client requests to the appropriate service.
- **Event Service** – Handles event creation, updates, deletion and attendee management.
- **User Service** – Manages user accounts and their event bookings.
- **Cli-Client** – Manages events and bookings through the API Gateway using REST calls.

---

## ⚙️ Technologies Used

| Layer | Technology |
|-------|-------------|
| Backend Framework | Spring Boot |
| API Communication | REST API (HTTP + JSON) |
| Inter-Service Communication | Spring Cloud OpenFeign |
| Database | MySQL |
| ORM | Spring Data JPA / JDBC |
| Build Tool | Maven |
| Language | Java |
| Server | Spring Boot Embedded Tomcat |

---

## 👩‍💻 Members Contributed
- **Harsh** - 2310990682
- **Hiten** - 2310990691
- **Manshika** - 2310990732
- **Navya** - 2310990749
