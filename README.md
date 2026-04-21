# BeanBoi Backend - Coffee Brew Tracker

A personal Spring Boot REST API application designed to track your coffee brewing experiments, manage bean inventory, store recipes, log purchases, and help you perfect your cup. Built with MongoDB to store all your brewing data locally or in a private database. Supports TypeScript + Next.js frontend with shadcn/ui.

## 🏗️ Tech Stack

- **Backend Framework**: Spring Boot 3.4.1
- **Database**: MongoDB 7.x (Docker container)
- **Backend Language**: Java 23 (Early Access)
- **Build Tool**: Gradle with Kotlin DSL
- **Containerization**: Docker & Docker Compose
- **Code Generation**: Project Lombok
- **Frontend**: TypeScript + Next.js + shadcn/ui (TypeScript)

## 📁 Core Features

This project helps you manage your entire coffee journey:

### Authentication & Authorization
- **Authentication Method**: Google OAuth 2.0 with JWT tokens
- **User Identification**: JWT token subject field maps to MongoDB user ID
- **Authentication Guard**: All endpoints require `@AuthenticationPrincipal Jwt`
- **User Service**: Maps JWT tokens to User entities via `getFromGoogleId(Jwt)` method
- **Automatic User Creation**: New users are created/updated when first accessing the API

### Bean Management
- Track coffee beans you own (roaster, origin, process, altitude, tasting notes)
- Monitor purchase frequency for each bean type
- Keep inventory of available beans
- User-scoped bean data (all beans belong to a specific user)

### Recipe Management  
- Create and store brewing recipes for different methods
- Support for Espresso recipes with stage-by-stage water flows
- Support for V60 pour-over recipes with water amount stages
- Customize temperature, ratio, duration, and brew phases
- Track recipe activity status (active/inactive)

### Purchase Tracking
- Record when you purchase coffee beans
- Track roast date and purchase date
- Store quantity purchased and price paid
- Monitor remaining inventory automatically
- Linked to specific beans via bean reference

### Grinder Management
- Track multiple grinders in your collection
- Record grinder settings for optimal results
- Link grind settings to specific recipes
- User-scoped grinder data

### Brew Logging
- Log each brewing session with detailed parameters
- Record dose-in, dose-out, temperature, duration, and brew date
- Add notes about taste and adjustments made
- Use different grinders for experiments
- Classify brews by type (Espresso, V60, etc.)

## 📁 Project Structure

The backend follows Spring's standard layered architecture:

```
beanBoiBackend/
├── build.gradle.kts
├── compose.yaml
├── Dockerfile
└── src/main/java/com/beanBoi/beanBoiBackend/beanBoiBackend/
    ├── beanBoiBackendApplication.java    # Main application entry point
    ├── WebConfig.java                    # Web configuration (CORS, security)
    └── core/                            # Core coffee brewing data layer
        ├── controllers/                 # REST API endpoints for CRUD operations
        │   ├── BeanController.java            # Manage your bean inventory
        │   ├── BeanPurchaseController.java    # Track purchases and inventory
        │   ├── GrinderController.java         # Manage grinder settings
        │   └── RecipeController.java          # Create/manage brewing recipes
        ├── models/                      # Domain entities (Bean, Brew, Recipe, etc.)
        ├── repositories/                # MongoDB data access layer
        ├── services/                    # Business logic and calculations (UserService)
        └── DTOs/                        # Request/Response objects
```

## 🔧 Prerequisites

Before running the application, ensure you have:

- **Java Development Kit (JDK) 23** or later (Early Access recommended)
- **Docker & Docker Compose** (for easy local setup with MongoDB)
- **Node.js + npm** (for building the TypeScript + Next.js frontend)
- **Git** (optional, for version control if desired)

## 🚀 Quick Start

### Backend Setup (Spring Boot)

**Option 1: Run with Docker Compose (Recommended)**

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

**Option 2: Run Locally with Gradle**

```bash
# Navigate to project directory
cd beanBoiBackend

# Build and run locally
./gradlew bootRun
```

The application will start at `http://localhost:8090`.

### Frontend Setup (TypeScript + Next.js + shadcn/ui)

```bash
# Initialize Next.js project
npx create-next-app@latest beanBoiFrontend --typescript --tailwind --app

# Install shadcn/ui
npx shadcn-ui@latest init

# Install authentication libraries
npm install @auth/next auth0 @tanstack/react-query

# Configure your API base URL
# Update .env.local in your frontend
VITE_API_BASE_URL=http://localhost:8090
```

**Frontend Integration Tips:**

- Use Axios or the `@tanstack/react-query` for API calls
- Implement authentication state with `@auth/next` or similar library
- Map JWT tokens to user data for protected routes
- Use shadcn/ui components for consistent UI design
- Store JWT tokens securely in cookies or localStorage

## 📦 Build Instructions

To create an executable JAR for backend deployment:

```bash
gradle build

# The JAR will be located in build/libs/beanBoiBackend-0.0.1-SNAPSHOT.jar
```

## 🗃️ Database Setup

The project uses MongoDB 7 with the following configuration:

- **Database**: `beanBoi`
- **Collections**: `users`, `beans`, `brews`, `recipes`, `grinders`, `bean_purchases`
- **Container Port**: `27017`
- **Persistence Volume**: Docker volume stores your brewing data between sessions

**Database Schemas:**

### Collection: `users`
- `id` (String) - MongoDB ObjectId
- `name` (String) - User's name
- `email` (String) - Google email address
- `recipies` (Array) - Recipe references
- `grinders` (Array) - Grinder references  
- `brews` (Array) - Brew references
- `beansOwned` (Array) - Owned bean references
- `beansAvailable` (Array) - Available bean references

### Collection: `beans`
- `id` (String) - MongoDB ObjectId
- `name` (String) - Bean name
- `tastingNotes` (String) - Tasting notes
- `roaster` (String) - Roaster name
- `process` (String) - Roasting process (e.g., washed, natural)
- `origin` (String) - Origin country/region
- `roastDegree` (Long) - Roast level
- `altitude` (Long) - Growing altitude
- `price` (Float) - Price per unit
- `timesPurchased` (Integer) - Purchase count
- `uid` (String) - User ID reference

### Collection: `brews`
- `id` (String) - MongoDB ObjectId
- `grindSetting` (String) - Grind setting description
- `notes` (String) - Brew notes/observations
- `uid` (String) - User ID reference
- `brewType` (Enum) - Brew type (Espresso, V60, etc.)
- `duration` (Float) - Duration in minutes/seconds
- `doseIn` (Float) - Dose-in weight in grams
- `doseOut` (Float) - Dose-out weight in grams
- `temperature` (Float) - Water temperature
- `brewDate` (DateTime) - Brew timestamp
- `grinderUsed` (Reference) - Grinder reference
- `coffeeUsed` (Reference) - Bean purchase reference

### Collection: `recipes`
- `id` (String) - MongoDB ObjectId
- `name` (String) - Recipe name
- `uid` (String) - User ID reference
- `description` (String) - Recipe description
- `temperature` (Double) - Temperature preference
- `duration` (Double) - Total duration
- `ratio` (Double) - Coffee to water ratio
- `isActive` (Boolean) - Active recipe flag

**Espresso Recipe** (extends Recipe):
- `waterFlow` (Array) - Water flow stages for espresso

**V60 Recipe** (extends Recipe):
- `waterAmount` (Array) - Water amount stages for V60

### Collection: `grinders`
- `id` (String) - MongoDB ObjectId
- `name` (String) - Grinder name
- `uid` (String) - User ID reference
- `grindSetting` (Array) - Grind setting options
- `grindSettingRequests` (Array) - Specific grind requests

### Collection: `bean_purchases`
- `id` (String) - MongoDB ObjectId
- `name` (String) - Purchase name
- `amountRemaining` (Float) - Remaining beans
- `amountPurchased` (Float) - Total amount purchased
- `beansPurchased` (Reference) - Bean reference
- `uid` (String) - User ID reference
- `purchaseDate` (Date) - Purchase date
- `roastDate` (Date) - Roast date
- `pricePaid` (Float) - Price per unit

## 🔐 Authentication Implementation

### OAuth 2.0 Flow

- **Identity Provider**: Google OAuth 2.0
- **Token Type**: JWT (JSON Web Token)
- **Token Storage**: Backend stores JWT in session (Spring Security)
- **Token Subject**: Maps to MongoDB user `id` field

### Authentication Endpoints

- **Login**: Redirect to Google OAuth for authentication
- **Token Validation**: Backend validates JWT tokens on each request
- **User Lookup**: `UserService.getFromGoogleId(Jwt)` retrieves user data
- **User Creation**: Automatic user creation on first request

### JWT Structure

```json
{
  "header": {
    "alg": "HS256",
    "typ": "JWT"
  },
  "payload": {
    "sub": "user_id_in_mongodb",
    "iss": "google-auth-server",
    "exp": 1700000000,
    "iat": 1700000000
  },
  "signature": "..."
}
```

## 📝 API Endpoints

All endpoints require authentication via `@AuthenticationPrincipal Jwt` parameter. Users are identified by the JWT token's `subject` field.

### Authentication
- **POST /oauth2/authorize** - Redirect to Google OAuth for login
- **POST /api/auth/login** - Handle OAuth callback, create JWT if needed

### User & Authentication
- **GET /users/{userId}/beans** - List beans owned by user
- **GET /users/{userId}/grinders** - List grinders for user
- **GET /users/{userId}/recipes** - List all recipes for user
- **GET /users/{userId}/recipes/active** - List active recipes only
- **GET /users/{userId}/beanPurchases** - List purchase history for user
- **GET /users/{userId}/brews** - List brew logs for user (if implemented)

### Bean Management
- **GET /beans/{beanId}/** - Get specific bean details
- **GET /users/{userId}/beans** - List beans owned by user
- **POST /users/{userId}/beans/** - Add new bean to inventory
- **PUT /users/{userId}/beans/{beanId}** - Update bean information
- **DELETE /users/{userId}/beans/{beanId}** - Remove bean from inventory

### Bean Purchase Tracking
- **GET /users/{userId}/beanPurchases/** - List purchase history
- **POST /users/{userId}/beanPurchases** - Record new purchase
- **PUT /users/{userId}/beanPurchases/{purchaseId}** - Edit purchase record
- **DELETE /users/{userId}/beanPurchases/{purchaseId}** - Remove or archive purchase

### Grinder Management
- **GET /users/{userId}/grinders** - List grinders for user
- **POST /users/{userId}/grinders** - Add new grinder
- **PUT /users/{userId}/grinders/{id}** - Update grinder settings
- **DELETE /users/{userId}/grinders/{id}** - Remove grinder

### Recipe Management (Espresso & V60)
- **GET /users/{userId}/recipes** - List all recipes
- **GET /users/{userId}/recipes/active** - List active recipes only
- **GET /users/{userId}/recipes/by-type/{type}** - Filter by type (Espresso/V60)
- **POST /users/{userId}/recipes** - Create new recipe
- **PUT /users/{userId}/recipes/{id}** - Update existing recipe
- **DELETE /users/{userId}/recipes/{id}** - Delete recipe

### Brew Logging
- **GET /brews** - List recorded brews (optional, may be implemented)
- **POST /brews** - Log a new brewing session
- **DELETE /brews/{id}** - Remove a brew entry

## 🧪 Testing

To run tests:

```bash
gradle test
```

The project includes unit tests for recipe calculations, brew data validation, and service layer logic.

## 🐳 Docker Details

The project includes ready-to-use Docker configuration:

- **Backend Image**: Spring Boot 3.4.1 on OpenJDK 23 (early access)
- **MongoDB Image**: mongo:7
- **Backend Port**: 8090
- **MongoDB Port**: 27017
- **Data Persistence**: MongoDB volume at `mongodb-data:/data/db` stores your brewing logs and inventory

## 📚 Frontend Integration Guide (TypeScript + Next.js + shadcn/ui)

### Prerequisites

- Node.js 18+ 
- npm or pnpm
- Backend running on `http://localhost:8090`

### Installation

```bash
# Initialize Next.js project with TypeScript and Tailwind
npx create-next-app@latest beanBoiFrontend --typescript --tailwind --app

# Navigate into the project directory
cd beanBoiFrontend

# Install shadcn/ui
npx shadcn-ui@latest init

# Install authentication library (@auth/next for Next.js 13+)
npm install @auth/next auth0 @tanstack/react-query axios

# Configure environment variables
cp .env.example .env.local

# Edit .env.local
VITE_API_BASE_URL=http://localhost:8090
NEXTAUTH_URL=http://localhost:3000
```

### TypeScript + Next.js Configuration

Create a `lib/api.ts` file for API interactions:

```typescript
// lib/api.ts
import axios from 'axios';

const api = axios.create({
  baseURL: process.env.VITE_API_BASE_URL || 'http://localhost:8090',
  headers: {
    'Content-Type': 'application/json',
  },
});

// Add JWT token to requests
api.interceptors.request.use((config) => {
  const token = document.cookie
    .split('; ')
    .find(c => c.startsWith('session='))
    ?.split('=')[1];
  
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export default api;
```

### Authentication with shadcn/ui

Use shadcn/ui components for authentication:

```typescript
// components/ui/button.tsx
import * as React from "react";
import { cva, type VariantProps } from "class-variance-authority";

export { cn } from "@/lib/utils";
import { Button } from "@/components/ui/button";
```

### API Response Handling

Use `@tanstack/react-query` for efficient API data fetching:

```typescript
// hooks/useBeans.ts
import { useQuery } from '@tanstack/react-query';
import api from '@/lib/api';

export function useBeans() {
  return useQuery({
    queryKey: ['beans'],
    queryFn: () => api.get('/users/beans').then(res => res.data),
  });
}
```

### Frontend-Backend Data Models

The frontend should use the following TypeScript interfaces for type-safe API interactions:

```typescript
// types/user.ts
export interface User {
  id: string;
  name: string;
  email: string;
  recipies: Recipe[];
  grinders: Grinder[];
  brews: Brew[];
  beansOwned: Bean[];
  beansAvailable: Bean[];
}

// types/bean.ts
export interface Bean {
  id: string;
  name: string;
  tastingNotes?: string;
  roaster?: string;
  process?: string;
  origin?: string;
  roastDegree: number;
  altitude: number;
  price: number;
  timesPurchased: number;
  uid: string;
}

// types/brew.ts
export interface Brew {
  id: string;
  grindSetting: string;
  notes: string;
  uid: string;
  brewType: BrewType;
  duration: number;
  doseIn: number;
  doseOut: number;
  temperature: number;
  brewDate: Date;
  grinderUsed?: Grinder;
  coffeeUsed?: BeanPurchase;
}

// types/recipe.ts
export interface Recipe {
  id: string;
  name: string;
  uid: string;
  description: string;
  temperature: number;
  duration: number;
  ratio: number;
  isActive: boolean;
  waterFlow?: WaterFlow[];
  waterAmount?: WaterAmount[];
}

// types/grinder.ts
export interface Grinder {
  id: string;
  name: string;
  uid: string;
  grindSetting: string[];
  grindSettingRequests?: Map<string, Object>[];
}

// types/beanPurchase.ts
export interface BeanPurchase {
  id: string;
  name: string;
  amountRemaining: number;
  amountPurchased: number;
  beansPurchased?: Bean;
  uid: string;
  purchaseDate: Date;
  roastDate: Date;
  pricePaid: number;
}

// types/water.ts
export interface WaterFlow {
  id: string;
  water: number;
  time: number;
}

export interface WaterAmount {
  id: string;
  water: number;
  time: number;
}

// types/brewType.ts
export enum BrewType {
  ESPRESSO = 'ESPRESSO',
  V60 = 'V60',
}
```

### Frontend Best Practices

1. **Use TypeScript Interfaces**: Match all API responses with typed interfaces
2. **Implement Loading States**: Use shadcn/ui `Spinner` or `Loading` component
3. **Handle Errors**: Use `@tanstack/react-query` error handling
4. **Protect Routes**: Implement route protection with authentication check
5. **Use shadcn/ui Components**: For buttons, inputs, tables, dialogs, etc.
6. **Follow Next.js Patterns**: Use App Router patterns, server components where possible

## 💡 Usage Tips

1. **Set up your inventory first**: Add coffee beans and their details
2. **Create your favorite recipes**: Store water ratios, temperatures, and timings for each brewing method
3. **Register your grinders**: Track different burr settings across multiple machines
4. **Log purchases**: Keep track of when you buy new batches and note roast dates
5. **Brew and document**: After each session, log the details that matter to you
6. **Review patterns**: Over time, identify which variables produce your preferred taste

## 🔗 External Services

- **OAuth Provider**: Google OAuth 2.0 (configured in Spring Security)
- **Authentication Service**: Google Identity Service via Spring Security
- **Database**: MongoDB (Docker container)

## 📄 License

This project is designed for personal coffee brewing tracking. Feel free to modify and extend as needed.

---

*Made with love for perfecting every cup. Enjoy your brewing journey!*