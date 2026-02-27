📚 Book Social Network – Backend
📌 Overview

Book Social Network is a secure RESTful backend application built with
Spring Boot 3 and
Spring Security 6.

It provides a robust API that allows users to manage their book collections and interact within a community of book enthusiasts.

Class Diagram:
<img src="assets/class-diagram.png" alt="Book Cover" width="200"/>
The backend is responsible for:

User registration and authentication

Secure email verification

Book management (create, update, share, archive)

Book borrowing and return workflow

Approval process for returned books

JWT-based security

REST API best practices implementation

🛠️ Tech Stack

Java 17+

Spring Boot 3

Spring Security 6

MySQL

JPA / Hibernate

JWT Authentication

Maven

RESTful API Architecture

🔐 Security Implementation

The application ensures security using:

JWT Token-based Authentication

Email verification before account activation

Role-based authorization

Encrypted password storage (BCrypt)

Secure endpoint protection with Spring Security filters

🚀 Core Features
👤 User Management

Register new users

Email validation before activation

Login with JWT authentication

Update user profile information

📚 Book Management

Create new books

Update book details

Archive books

Share books with the community

View available books

🔄 Borrowing System

Borrow books (with availability validation)

Return borrowed books

Approve returned books

Prevent borrowing of unavailable books

🗂️ Project Structure
src/main/java/com/example/booksocialnetwork
│
├── config
├── controller
├── service
├── repository
├── entity
├── security
└── exception
⚙️ Database Configuration (MySQL)

Update your application.yml file:

spring:
datasource:
url: jdbc:mysql://localhost:3306/book_social_network
username: your_username
password: your_password
jpa:
hibernate:
ddl-auto: update
show-sql: true
properties:
hibernate:
dialect: org.hibernate.dialect.MySQLDialect
