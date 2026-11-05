# Appointment Booking API

A Java 21 / Spring Boot 3.5 REST backend for the five static booking frontend concepts. It uses no frontend or JavaScript framework. Local development runs with an in-memory H2 database; Docker Compose runs the API with PostgreSQL.

## Run locally with H2

Requirements: JDK 21 and Maven 3.6.3+.

```sh
mvn spring-boot:run
```

The API starts at `http://localhost:8080`. Demo staff and services are seeded automatically. H2 data is in-memory and resets when the app restarts. The H2 console is at `http://localhost:8080/h2-console` with JDBC URL `jdbc:h2:mem:appointments`, user `sa`, and an empty password.

## Run with PostgreSQL in Docker

From this directory:

```sh
docker compose up --build
```

This starts PostgreSQL and the API. The database data is stored in a Docker volume. Stop with `docker compose down`; remove the volume too only when you intend to erase the database, using `docker compose down -v`.

## Connect a frontend

Run the API, then serve any frontend folder over HTTP. For example, from `booking-frontends/01-atelier-salon`:

```sh
python -m http.server 5500
```

Open `http://localhost:5500`. The frontend requests `http://localhost:8080/api`; if the API is unavailable it falls back to preview data. CORS is configured for localhost ports 5173 and 5500 through 5504. You can override the API URL per frontend with a query string, for example `http://localhost:5500/?api=http://localhost:8080/api`.

## API

| Method | Endpoint | Purpose |
|---|---|---|
| GET | `/api/services` | List services; optional `q`, `category`, and `staffId` filters |
| GET | `/api/categories` | List service categories |
| GET | `/api/staff` | List specialists |
| GET | `/api/availability?serviceId=1&staffId=1&date=2026-12-10` | Return available appointment times |
| POST | `/api/bookings` | Create an appointment booking |

Example booking request:

```json
{
  "serviceId": 2,
  "staffId": 2,
  "date": "2026-12-10",
  "time": "10:30",
  "clientName": "Alex Morgan",
  "clientEmail": "alex@example.com"
}
```

A successful request returns `201 Created` with the booking details. Invalid input returns `400`; a slot already booked for that specialist returns `409 Conflict`. A database unique constraint protects each specialist/date/time combination from duplicate bookings.

## Scope

This is a runnable demo backend with seeded example services. Booking confirmations are stored, but authentication, payment processing, email delivery, staff-specific service eligibility, business opening hours, cancellation, and production database secrets are not configured. Set real credentials through environment variables before deployment.
