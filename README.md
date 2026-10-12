# Online Store REST API

A backend REST API for an online store, built with Java and Spring Boot. The project focuses on common e-commerce workflows such as product management, shopping carts, order processing, coupons, and product reviews.

## Features

- **Product catalogue** — paginated product listing, filtering by product type, and product details.
- **Product management** — create, replace, and partially update products.
- **Shopping cart** — add products, inspect the current user's cart, change quantities, and remove products.
- **Order processing** — create an order from the cart, calculate totals, apply coupons, and view order history.
- **Order status management** — support for order statuses such as `PENDING`, `CONFIRMED`, `SHIPPED`, `DELIVERED`, and `CANCELLED`.
- **Coupons** — create discount coupons, check their validity, and calculate discounted totals.
- **Product reviews** — review eligible purchased products and maintain aggregate product ratings.
- **Validation and error handling** — request DTO validation and centralized REST exception responses.
- **Persistence and transaction handling** — PostgreSQL, Spring Data JPA, transactions, and database locking for selected operations.
- **Authentication and authorization** — Spring Security and JWT integration; this part of the project is still being developed.

## Tech Stack

- Java 21
- Spring Boot 4.1.0
- Spring Web MVC
- Spring Data JPA / Hibernate
- PostgreSQL
- Jakarta Bean Validation
- Spring Security
- JSON Web Tokens (JJWT)
- Maven Wrapper
- JUnit, Spring Boot Test, and Mockito-based service tests

## Architecture

The source code is organised into layers:

| Package | Responsibility |
| --- | --- |
| `controller` | REST endpoints and request handling |
| `service` | Business logic and transaction boundaries |
| `repository` | Database access through Spring Data JPA |
| `entity` | JPA entities mapped to database tables |
| `dto` | Request and response data transfer objects |
| `mapper` | Conversion between entities and DTOs |
| `exception` | Centralized REST exception handling |
| `security` | Authentication, JWT, user details, and role-related code |

## Getting Started

### Prerequisites

Install the following:

- JDK 21
- PostgreSQL
- Git

A separate Maven installation is not required because the repository includes the Maven Wrapper.

### 1. Clone the repository

```bash
git clone https://github.com/Molim1234/najvecaLolcina1.git
cd najvecaLolcina1
```

### 2. Configure PostgreSQL

The application configuration is located at:

`src/main/resources/application.properties`

The current datasource URL points to a local PostgreSQL instance and the database named `postgres`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/postgres
spring.datasource.username=YOUR_POSTGRES_USERNAME
spring.datasource.password=YOUR_POSTGRES_PASSWORD
spring.jpa.hibernate.ddl-auto=update
```

Update the URL, username, and password to match your local PostgreSQL setup. Make sure the database named in the URL already exists. The `ddl-auto=update` setting lets Hibernate update the schema during development; it is not a replacement for a production database migration strategy.

The application also uses a JWT signing secret. For anything beyond local development, supply database credentials and JWT secrets through environment variables or a secret manager instead of committing real values to Git. If any credential or secret currently in the repository is real or has been reused elsewhere, rotate it.

### 3. Run the application

On Linux or macOS:

```bash
./mvnw spring-boot:run
```

On Windows:

```bat
mvnw.cmd spring-boot:run
```

When the application starts successfully, the API is available at:

`http://localhost:8080`

### 4. Run the tests

On Linux or macOS:

```bash
./mvnw test
```

On Windows:

```bat
mvnw.cmd test
```

To build the project:

```bash
./mvnw clean package
```

## API Overview

The table below lists the main routes visible in the current controllers. Some routes require authentication, and product/order/coupon administration routes are intended for administrators.

### Authentication

| Method | Endpoint | Description |
| --- | --- | --- |
| `POST` | `/register` | Register a user |
| `POST` | `/login` | Authenticate and receive a JWT |

For endpoints requiring authentication, send the token in the header:

```http
Authorization: Bearer <your-jwt-token>
```

### Products

| Method | Endpoint | Description |
| --- | --- | --- |
| `GET` | `/api/products` | List products; supports pagination and an optional `type` filter |
| `GET` | `/api/products/{id}` | Get one product |
| `POST` | `/api/admin/products` | Create a product (admin) |
| `PUT` | `/api/admin/products/{id}` | Replace/update a product (admin) |
| `PATCH` | `/api/admin/products/{id}` | Partially update a product (admin) |

Spring Data pagination parameters such as `page`, `size`, and `sort` can be passed to the product listing endpoint.

### Shopping Cart

| Method | Endpoint | Description |
| --- | --- | --- |
| `GET` | `/api/cart` | View the authenticated user's cart |
| `POST` | `/api/cart/items` | Add a product to the cart |
| `DELETE` | `/api/cart/items/{id}` | Remove a product from the cart; the path value is the product ID |
| `PATCH` | `/api/cart/items` | Decrease the requested product quantity or remove it |
| `PATCH` | `/api/cart/items/minusOne/{productId}` | Decrease quantity by one |
| `PATCH` | `/api/cart/items/plusOne/{productId}` | Increase quantity by one, subject to stock availability |

Example request to add a product:

```http
POST /api/cart/items
Content-Type: application/json

{
  "productId": 1,
  "quantity": 2
}
```

### Orders and Coupons

| Method | Endpoint | Description |
| --- | --- | --- |
| `POST` | `/api/orders` | Create an order from the current cart; optional `coupon` query parameter |
| `GET` | `/api/orders` | View the authenticated user's orders |
| `GET` | `/api/orders/{id}` | View the products in one of the user's orders |
| `POST` | `/api/coupons` | Check a coupon and calculate its discounted cart total |
| `GET` | `/api/admin/orders` | List all orders (admin) |
| `POST` | `/api/admin/orders/{id}/status?status=CONFIRMED` | Change an order's status (admin) |
| `POST` | `/api/admin/coupons` | Create a coupon (admin) |

The order-creation endpoint accepts shipping-address details as JSON and creates the order using the current user's cart. To apply a coupon, pass its code through the optional `coupon` query parameter.

### Reviews

| Method | Endpoint | Description |
| --- | --- | --- |
| `POST` | `/reviews` | Submit a product review |

Example review request:

```json
{
  "productId": 1,
  "rate": 5,
  "description": "Good product"
}
```

Review eligibility is checked in the service layer. The rating is validated to be between 1 and 5.

## Validation and Error Responses

Request DTOs use Jakarta Bean Validation annotations such as `@NotBlank`, `@Positive`, `@PositiveOrZero`, `@Email`, and `@Valid`. A centralized `@RestControllerAdvice` handles validation failures and common application exceptions.

Typical response categories include:

- `400 Bad Request` — invalid request body or validation failure
- `404 Not Found` — requested resource does not exist
- `409 Conflict` — conflict with the current state or a database constraint
- `500 Internal Server Error` — unexpected server-side error

## Project Structure

```text
src/
├── main/
│   ├── java/com/example/najvecaLolcina/
│   │   ├── controller/
│   │   ├── dto/
│   │   ├── entity/
│   │   ├── enumm/
│   │   ├── exception/
│   │   ├── mapper/
│   │   ├── repository/
│   │   ├── security/
│   │   └── service/
│   └── resources/
│       └── application.properties
└── test/
    └── java/com/example/najvecaLolcina/
```

## Notes

This repository is a portfolio and learning project. The API and its business rules may evolve as the project is improved. Before using it in production, review security configuration, externalize secrets, use database migrations, and add integration tests for critical workflows.
