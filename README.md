# FinTrack Pro - Backend

Spring Boot REST API for expense tracking application.

## Tech Stack
- Java 17+
- Spring Boot 3.x
- Spring Security (JWT)
- PostgreSQL
- Hibernate/JPA

## Setup
1. Install Java 17+
2. Configure database in `application.properties`
3. Run: `mvn clean install && mvn spring-boot:run`

## API Endpoints
- POST `/api/auth/register` - Register user
- POST `/api/auth/login` - Login user
- POST `/api/expenses` - Add expense
- GET `/api/expenses` - Get all expenses
- DELETE `/api/expenses/{id}` - Delete expense

## Deployment
Deployed on Railway: [Your live URL here]
