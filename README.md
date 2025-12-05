# QuantaFeed - Real-Time Market Data Ingestion Service

A lightweight real-time market data ingestion and query service built with Spring Boot 4.0.0 and Java 21.

## Overview

QuantaFeed simulates live price ticks, buffers them using a producer–consumer architecture, persists them into an embedded H2 database, and exposes REST APIs to fetch market data.

## Architecture

```
┌─────────────────┐     ┌──────────────────┐     ┌─────────────────┐
│  TickProducer   │────▶│  BlockingQueue   │────▶│  TickConsumer   │
│  (Scheduled)    │     │  (Buffer: 1000)  │     │  (Scheduled)    │
└─────────────────┘     └──────────────────┘     └────────┬────────┘
                                                          │
                                                          ▼
┌─────────────────┐     ┌──────────────────┐     ┌─────────────────┐
│  TickController │◀───▶│   TickService    │◀───▶│ TickRepository  │
│  (REST API)     │     │  (Business Logic)│     │  (JPA/H2)       │
└─────────────────┘     └──────────────────┘     └─────────────────┘
```

## Project Structure

```
src/main/java/com/QuantaFeed/marketstream/
├── QuantaFeedApplication.java    # Main application with @EnableScheduling
├── config/
│   └── TickQueueConfig.java      # BlockingQueue bean configuration
├── controller/
│   └── TickController.java       # REST API endpoints
├── ingestion/
│   ├── IngestionScheduler.java   # Scheduled producer/consumer coordination
│   ├── TickConsumer.java         # Drains queue and persists to DB
│   └── TickProducer.java         # Simulates market ticks
├── model/
│   └── Tick.java                 # JPA entity (symbol, price, timestamp)
├── repository/
│   └── TickRepository.java       # Spring Data JPA repository
└── service/
    └── TickService.java          # Business logic layer
```

## Features

- **Real-time tick simulation** - Generates random price ticks for AAPL, GOOG, MSFT, META, NVDA
- **Producer-Consumer pattern** - Uses `BlockingQueue` for buffering ticks
- **Scheduled ingestion** - Produces ticks every 100ms, consumes every 500ms
- **H2 embedded database** - In-memory persistence with web console
- **REST API** - Query latest ticks, historical data, and more

## REST API Endpoints

| Method | Endpoint                     | Description                          |
|--------|------------------------------|--------------------------------------|
| GET    | `/api/ticks/latest/{symbol}` | Get the latest tick for a symbol     |
| GET    | `/api/ticks/recent/{symbol}` | Get the last 100 ticks for a symbol  |
| GET    | `/api/ticks/history`         | Get historical ticks with time range |

### Example Requests

```bash
# Get latest tick for AAPL
curl http://localhost:8080/api/ticks/latest/AAPL

# Get recent ticks for GOOG
curl http://localhost:8080/api/ticks/recent/GOOG

# Get historical ticks
curl "http://localhost:8080/api/ticks/history?symbol=MSFT&start=2024-01-01T00:00:00Z&end=2024-12-31T23:59:59Z"
```

## Getting Started

### Prerequisites

- Java 21
- Maven 3.9+

### Build

```bash
./mvnw clean package
```

### Run

```bash
./mvnw spring-boot:run
```

Or run the JAR directly:

```bash
java -jar target/marketstream-0.0.1-SNAPSHOT.jar
```

### Access H2 Console

Navigate to: http://localhost:8080/h2-console

- **JDBC URL**: `jdbc:h2:mem:QuantaFeed`
- **Username**: `sa`
- **Password**: (empty)

## Configuration

Key properties in `application.yml`:

```yaml
app:
  symbols: AAPL,GOOG,MSFT,META,NVDA    # Symbols to simulate
  queue:
    capacity: 1000                      # Buffer size
  ingestion:
    produce-rate: 200                   # Produce tick every 200ms
    consume-rate: 500                   # Persist batch every 500ms
```

## Tech Stack

- **Java 21**
- **Spring Boot 4.0.0**
- **Spring Data JPA**
- **H2 Database**
- **Lombok**

## License

MIT

