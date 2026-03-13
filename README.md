# BeanBoi Backend - Coffee Brew Tracker

A Spring Boot application designed to track your coffee brewing experiments, log brew parameters, and help you perfect your cup. Built with MongoDB to store your brewing data locally or in a private database.

## 🏗️ Tech Stack

- **Framework**: Spring Boot 3.4.1
- **Database**: MongoDB 7.x (local Docker container)
- **Language**: Java 23 (Early Access)
- **Build Tool**: Gradle with Kotlin DSL
- **Containerization**: Docker & Docker Compose
- **Code Generation**: Project Lombok

## 📁 Core Features

This project is designed to help you:
- Log coffee brews (water temperature, grind size, ratio, time, etc.)
- Track different brewing methods (pour-over, French press, espresso, etc.)
- Monitor extraction yield and TDS readings
- Analyze your brewing patterns over time
- Export your data for further analysis

## 📁 Project Structure

The backend follows Spring's standard layered architecture:

```
src/main/java/com/beanBoi/beanBoiBackend/beanBoiBackend/
├── BeanBoiBackendApplication.java    # Main application entry point
├── WebConfig.java                    # Web configuration (CORS, Security)
└── core/                            # Core brewing data layer
    ├── controllers/                 # Brew API endpoints
    ├── models/                      # Brew entities and configurations
    ├── repositories/                # Data access layer for MongoDB
    ├── services/                    # Brewing calculation logic
    └── DTOs/                        # Request/Response objects
```

## 🔧 Prerequisites

Before running the application, ensure you have:

- **Java Development Kit (JDK) 23** or later
- **Docker & Docker Compose** (for easy local setup with MongoDB)
- **Git** (optional, for version control if desired)

## 🚀 Quick Start

### Option 1: Run with Docker Compose (Recommended)

This sets up both the Spring Boot app and MongoDB with a single command:

```bash
# Start services
docker compose up -d --build

# View logs
docker compose logs -f

# Stop services
docker compose down
```

The application will be accessible at `http://localhost:8090` and MongoDB runs on `localhost:27017`.

### Option 2: Run Locally with Gradle

```bash
# Navigate to project directory
cd beanBoiBackend

# Build and run locally
./gradlew bootRun
```

The application will start at `http://localhost:8090`.

## 📦 Build Instructions

To create an executable JAR for deployment:

```bash
gradle build

# The JAR will be located in build/libs/beanBoiBackend-0.0.1-SNAPSHOT.jar
```

## 🗃️ Database Setup

The project uses MongoDB with the following configuration:

- **Database**: `beanBoi`
- **Container Port**: `27017`
- **Persistence Volume**: Docker volume stores your brewing data between sessions

To run MongoDB locally without Docker (for development):

```bash
# Install MongoDB locally or use a local instance
# The app will connect to it via environment variables in .env file
```

## 🔐 Environment Configuration

Create a `.env` file in the project root with your preferences:

```env
# Application settings
SPRING_DATASOURCE_URL=mongodb://localhost:27017/beanBoi
SPRING_JAVA_TOOLCHAIN=java23

# API Settings
API_PORT=8090
```

The Docker Compose setup will handle creating the default `.env` with `beanBoi` database.

## 📝 API Endpoints

The REST API follows RESTful conventions for your brewing data:

| Method | Path | Description |
|--------|------|-------------|
| GET    | /api/recipes | List all coffee recipes |
| GET    | /api/recipes/{id} | Get specific recipe details |
| POST   | /api/recipes | Create new brewing recipe |
| GET    | /api/brews | List all recorded brews |
| POST   | /api/brews | Log a new coffee brew |
| DELETE | /api/brews/{id} | Remove a brew entry |

## 🧪 Testing

To run tests:

```bash
gradle test
```

The project includes unit tests for brewing calculations and data validation.

## 🐳 Docker Details

The project includes ready-to-use Docker configuration:

- **Base Image**: `openjdk:23-ea-18-jdk-slim`
- **Application Port**: 8090
- **MongoDB Port**: 27017 (Docker managed)
- **Data Persistence**: MongoDB volume stores your brewing logs

## 📚 Coffee Brew Data Model

Track these parameters for each brew:

- **Recipe**: Coffee bean variety, roast level, origin
- **Method**: Pour-over, French press, AeroPress, V60, Chemex, etc.
- **Water Temp**: Degrees Celsius or Fahrenheit
- **Grind Size**: Fine, medium, coarse, or custom microns
- **Ratio**: Water to coffee ratio (e.g., 1:15)
- **Brew Time**: Total extraction time
- **Yield**: Total output in grams/ounces
- **Notes**: Taste observations, adjustments made

## 💡 Usage Tips

1. Start by creating recipes for your favorite coffee beans
2. Log each brew session to track how parameters affect taste
3. Use Docker Compose for easy local development with persistent data
4. Review logs to identify which brewing methods produce your preferred taste profile

---

*Made with love for perfecting every cup. Enjoy your brewing journey!*
