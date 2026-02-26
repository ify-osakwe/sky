# Sky — Weather Forecast App

**Sky** is a full-stack weather forecast application built with Spring Boot and Thymeleaf. It provides real-time weather data and multi-day forecasts for any city worldwide, powered by the [OpenWeatherMap API](https://openweathermap.org/api).

> **Live Demo:** [sky-demo](https://sky-production-9387.up.railway.app/)
> **API Docs:** [sky-api-docs](https://sky-production-9387.up.railway.app/swagger-ui/index.html)

---

## Key Features

| Feature                     | Description                                                                                                                     |
| --------------------------- | ------------------------------------------------------------------------------------------------------------------------------- |
| **City Weather Search**     | Look up current weather conditions for any city using the Geocoding API for precise location resolution.                        |
| **Multi-Day Forecast**      | View upcoming weather forecasts including temperature, humidity, wind, and precipitation probability.                           |
| **Smart Caching**           | Caffeine-backed caching (15 min for current weather, 1 hr for forecasts, 24 hr for city lookups) minimizes redundant API calls. |
| **Background Data Refresh** | A scheduled task automatically refreshes weather data for the top 10 most-searched cities every 30 minutes.                     |
| **REST API**                | Fully documented JSON API available alongside the web UI — no authentication required.                                          |

---

## Tech Stack

| Layer                | Technology                                    |
| -------------------- | --------------------------------------------- |
| **Language**         | Java 17                                       |
| **Framework**        | Spring Boot 3.5.10                            |
| **View Engine**      | Thymeleaf (server-side rendering)             |
| **CSS Framework**    | Bootstrap 5.3 + Inter font + custom CSS       |
| **Database**         | PostgreSQL 16                                 |
| **ORM**              | Spring Data JPA / Hibernate                   |
| **Migrations**       | Flyway                                        |
| **HTTP Client**      | Spring `RestClient`                           |
| **Caching**          | Spring Cache + Caffeine.                      |
| **API Docs**         | Springdoc OpenAPI (Swagger UI)                |
| **Build Tool**       | Maven                                         |
| **Containerization** | Docker (multi-stage) + Docker Compose         |
| **Deployment**       | Railway                                       |

---

## Architecture Overview

Sky follows the **Model-View-Controller (MVC)** pattern with clearly separated layers:

```
┌─────────────────────────────────────────────────────────────────┐
│                         Sky Application                         │
│                                                                 │
│  ┌──────────────────┐  ┌───────────────┐  ┌──────────────────┐  │
│  │   Controllers    │  │   Services    │  │  Repositories    │  │
│  │                  │─▶│               │─▶│                  │  │
│  │ WeatherWeb       │  │ WeatherService│  │ CityRepo         │  │
│  │ WeatherApi       │  │ CityService   │  │ CurrentWeather   │  │
│  │ CityApi          │  │ OpenWeatherMap│  │ ForecastRepo     │  │
│  │                  │  │ WeatherRefresh│  │                  │  │
│  └────────┬─────────┘  └───────┬───────┘  └────────┬─────────┘  │
│           │                    │                   │            │
│           ▼                    │                   ▼            │
│  ┌──────────────────┐          │          ┌──────────────────┐  │
│  │    Thymeleaf     │          │          │   PostgreSQL     │  │
│  │    Templates     │          │          │   (via Flyway)   │  │
│  │                  │          │          │                  │  │
│  │ home.html        │          │          │ City             │  │
│  │ search.html      │          │          │ CurrentWeather   │  │
│  │ weather.html     │          │          │ Forecast         │  │
│  │ error.html       │          │          │                  │  │
│  └──────────────────┘          │          └──────────────────┘  │
│                                ▼                                │
│                     ┌───────────────────┐                       │
│                     │  OpenWeatherMap   │                       │
│                     │  Geocoding + API  │                       │
│                     └───────────────────┘                       │
└─────────────────────────────────────────────────────────────────┘
```

**Request flow:**

1. **Controllers** handle incoming HTTP requests (web pages or REST API).
2. **Services** encapsulate business logic, coordinate API calls, and manage caching.
3. **Repositories** (Spring Data JPA) perform database operations.
4. **Thymeleaf Templates** render server-side HTML for the web UI.

---

## Installation & Setup

### 1. Clone the Repository

```bash
git clone https://github.com/ify-osakwe/sky.git
cd sky
```

### 2. Create a PostgreSQL Database

```sql
CREATE DATABASE skydb;
```

### 3. Configure Environment Variables

Create a `.env` file in the project root (it is auto-imported via `spring.config.import`):

```properties
DB_URL=jdbc:postgresql://localhost:5432/skydb
DB_USERNAME=your_db_username
DB_PASSWORD=your_db_password
OPENWEATHERMAP_API_KEY=your_openweathermap_api_key
```

### 4. Run Database Migrations

Flyway migrations run automatically on application startup. No manual step is required. The migration scripts are located in `src/main/resources/db/migration/`.

### 5. Start the Application

```bash
./mvnw spring-boot:run
```

The application will be available at **[http://localhost:8080](http://localhost:8080)**.

---

## Environment Variables

All configuration is managed through `application.properties` with environment variable placeholders. The required keys are:

| Variable                 | Description                           | Example                                  |
| ------------------------ | ------------------------------------- | ---------------------------------------- |
| `DB_URL`                 | JDBC connection string for PostgreSQL | `jdbc:postgresql://localhost:5432/skydb` |
| `DB_USERNAME`            | Database username                     | `postgres`                               |
| `DB_PASSWORD`            | Database password                     | `secret`                                 |
| `OPENWEATHERMAP_API_KEY` | API key from OpenWeatherMap           | `abc123def456`                           |

### Optional Configuration (in `application.properties`)

| Property                                | Default             | Description                          |
| --------------------------------------- | ------------------- | ------------------------------------ |
| `sky.cache.current-weather-ttl-minutes` | `15`                | TTL for current weather cache        |
| `sky.cache.forecast-ttl-minutes`        | `60`                | TTL for forecast data cache          |
| `sky.cache.city-search-ttl-hours`       | `24`                | TTL for city search cache            |
| `sky.cache.max-size`                    | `100`               | Max cache entries                    |
| `sky.scheduler.refresh-interval-ms`     | `1800000`           | Weather refresh interval (30 min)    |
| `sky.scheduler.refresh-city-count`      | `10`                | Number of top cities to auto-refresh |
| `sky.scheduler.cleanup-cron`            | `0 0 3 * * ?`       | Daily cleanup of stale data (3 AM)   |
| `sky.scheduler.stale-data-days`         | `30` (`15` in prod) | Days before data is considered stale |

---

## Deviation from TDD & PRD

> [!IMPORTANT]
> The [TDD](./TDD.md) and [PRD](./PRD.md) were written assuming weather data can be fetched by passing a city name directly to the OpenWeatherMap `/weather` and `/forecast` endpoints (e.g., `?q=Berlin`).
>
> **The codebase has intentionally deviated from this** by adopting the **Geocoding API + lat/lon approach**, which is the modern way to use the OpenWeatherMap API.

**What this means in practice:**

1. When a user searches for a city, Sky first calls the **Geocoding API** to resolve the city name into geographic coordinates (latitude & longitude).
2. The resolved coordinates are then passed to the `/weather` and `/forecast` endpoints (e.g., `?lat=52.52&lon=13.41`) for precise weather data retrieval.

This approach provides **more accurate results**, avoids ambiguity with common city names, and aligns with OpenWeatherMap's latest API guidelines.
