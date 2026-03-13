# BeanBoi Backend - Coffee Brew Tracker

A personal Spring Boot REST API application designed to track your coffee brewing experiments, manage bean inventory, store recipes, log purchases, and help you perfect your cup. Built with MongoDB to store all your brewing data locally or in a private database.

## 🏗️ Tech Stack

- **Framework**: Spring Boot 3.4.1
- **Database**: MongoDB 7.x (Docker container)
- **Language**: Java 23 (Early Access)
- **Build Tool**: Gradle with Kotlin DSL
- **Containerization**: Docker & Docker Compose
- **Code Generation**: Project Lombok

## 📁 Core Features

This project helps you manage your entire coffee journey:

### Bean Management
- Track coffee beans you own (roaster, origin, process, altitude, tasting notes)
- Monitor purchase frequency for each bean type
- Keep inventory of available beans

### Recipe Management  
- Create and store brewing recipes for different methods
- Support for Espresso recipes with stage-by-stage water flows
- Support for V60 pour-over recipes with water amount stages
- Customize temperature, ratio, duration, and brew phases

### Purchase Tracking
- Record when you purchase coffee beans
- Track roast date and purchase date
- Store quantity purchased and price paid
- Monitor remaining inventory automatically

### Grinder Management
- Track multiple grinders in your collection
- Record grinder settings for optimal results
- Link grind settings to specific recipes

### Brew Logging
- Log each brewing session with detailed parameters
- Record dose-in, dose-out, temperature, duration, and brew date
- Add notes about taste and adjustments made
- Use different grinders for experiments

## 📁 Project Structure

The backend follows Spring's standard layered architecture:

```
src/main/java/com/beanBoi/beanBoiBackend/beanBoiBackend/
├── BeanBoiBackendApplication.java    # Main application entry point
├── WebConfig.java                    # Web configuration (CORS for Flutter frontend)
└── core/                            # Core coffee brewing data layer
    ├── controllers/                 # REST API endpoints for CRUD operations
    │   ├── BeanController.java            # Manage your bean inventory
    │   ├── BeanPurchaseController.java    # Track purchases and inventory
    │   ├── GrinderController.java         # Manage grinder settings
    │   └── RecipeController.java          # Create/manage brewing recipes
    ├── models/                      # Domain entities (Bean, Brew, Recipe, etc.)
    ├── repositories/                # MongoDB data access layer
    ├── services/                    # Business logic and calculations
    └── DTOs/                        # Request/Response objects
```

## 🔧 Prerequisites

Before running the application, ensure you have:

- **Java Development Kit (JDK) 23** or later (Early Access recommended)
- **Docker & Docker Compose** (for easy local setup with MongoDB)
- Git (optional, for version control if desired)

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

The project uses MongoDB 7 with the following configuration:

- **Database**: `beanBoi`
- **Collection Examples**: `beans`, `brews`, `recipes`, `grinders`, `bean_purchases`, `users`
- **Container Port**: `27017`
- **Persistence Volume**: Docker volume stores your brewing data between sessions

To run MongoDB locally without Docker (for development):

```bash
# Install MongoDB locally or use a local instance
# The app will connect to it via environment variables in .env file
```

## 🔐 Environment Configuration

The Docker Compose setup automatically creates a `.env` file with default configuration. For custom settings:

```env
# Application settings
SPRING_DATASOURCE_URL=mongodb://localhost:27017/beanBoi
SPRING_JAVA_TOOLCHAIN=java23

# API Settings
API_PORT=8090
```

## 📝 API Endpoints

The REST API follows RESTful conventions for your coffee brewing data:

### Bean Management
- `GET /users/{userId}/beans` - List beans owned by user
- `GET /beans/{beanId}` - Get specific bean details  
- `POST /users/{userId}/beans/` - Add new bean to inventory
- `PUT /users/{userId}/beans/{beanId}` - Update bean information
- `DELETE /users/{userId}/beans/{beanId}` - Remove bean from inventory

### Bean Purchase Tracking
- `GET /users/{userId}/beanPurchases` - List purchase history
- `POST /users/{userId}/beanPurchases` - Record new purchase
- `PUT /users/{userId}/beanPurchases/{purchaseId}` - Edit purchase record
- `DELETE /users/{userId}/beanPurchases/{purchaseId}` - Remove or archive purchase

### Grinder Management
- `GET /users/{uid}/grinders` - List grinders for user
- `POST /users/{uid}/grinders` - Add new grinder
- `PUT /users/{uid}/grinders/{id}` - Update grinder settings
- `DELETE /users/{uid}/grinders/{id}` - Remove grinder

### Recipe Management (Espresso & V60)
- `GET /users/{uid}/recipes` - List all recipes
- `GET /users/{uid}/recipes/active` - List active recipes only
- `GET /users/{uid}/recipes/by-type/{type}` - Filter by type (Espresso/V60)
- `POST /users/{uid}/recipes` - Create new recipe
- `PUT /users/{uid}/recipes/{id}` - Update existing recipe
- `DELETE /users/{uid}/recipes/{id}` - Delete recipe

### Brew Logging
- `GET /brews` - List recorded brews
- `POST /brews` - Log a new brewing session
- `DELETE /brews/{id}` - Remove a brew entry

## 🧪 Testing

To run tests:

```bash
gradle test
```

The project includes unit tests for recipe calculations, brew data validation, and service layer logic.

## 🐳 Docker Details

The project includes ready-to-use Docker configuration:

- **Base Image**: `openjdk:23-ea-18-jdk-slim`
- **Application Port**: 8090
- **MongoDB Port**: 27017 (Docker managed)
- **Data Persistence**: MongoDB volume at `/data/db` stores your brewing logs and inventory

## 📚 Data Models Overview

### Brew Model
Track these parameters for each brew session:
- Grind setting
- Notes and observations
- User ID reference
- Brew type classification
- Duration in minutes/seconds
- Dose-in and dose-out (grams)
- Water temperature
- Brew timestamp
- Grinder used
- Coffee bean purchased

### Recipe Model  
Create recipes with these specifications:
- Name and description
- Temperature preference
- Ratio (coffee to water)
- Total duration
- Water flow/amount stages by phase
- Type: Espresso or V60

### User Model
Organize your collection with:
- User name
- List of saved recipes
- Registered grinders
- Logged brew sessions
- Owned beans
- Available purchases

## 💡 Usage Tips

1. **Set up your inventory first**: Add coffee beans and their details
2. **Create your favorite recipes**: Store water ratios, temperatures, and timings for each brewing method
3. **Register your grinders**: Track different burr settings across multiple machines
4. **Log purchases**: Keep track of when you buy new batches and note roast dates
5. **Brew and document**: After each session, log the details that matter to you
6. **Review patterns**: Over time, identify which variables produce your preferred taste

---

*Made with love for perfecting every cup. Enjoy your brewing journey! *
