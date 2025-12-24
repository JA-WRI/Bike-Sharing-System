# Bike Sharing System (BIXI-style)
[![CI](https://img.shields.io/github/actions/workflow/status/JA-WRI/Bike-Sharing-System/ci.yml?style=for-the-badge&logo=githubactions&logoColor=white)](https://github.com/JA-WRI/Bike-Sharing-System/actions)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F.svg?style=for-the-badge&logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![MongoDB](https://img.shields.io/badge/MongoDB-47A248.svg?style=for-the-badge&logo=mongodb&logoColor=white)](https://www.mongodb.com/)
[![React](https://img.shields.io/badge/React-61DAFB.svg?style=for-the-badge&logo=react&logoColor=white)](https://reactjs.org/)

A full-stack bike-sharing web application inspired by real-world systems like **BIXI**, designed with a scalable backend, secure authentication, and a modern frontend.
Users can authenticate, locate available bikes, rent and return them, and complete payments securely. The system was built to simulate a **real-world production environment**, focusing on clean architecture, RESTful APIs, and third-party integrations.

## Tech Stack

| Layer | Technologies |
|------|-------------|
| Frontend | React, JavaScript |
| Backend | Java, Spring Boot, MongoDB |
| Auth | Google OAuth 2.0, JWT |
| Testing | JUnit, Mockito |
| CI/CD | GitHub Actions |




## Key Features

### 🔐 User Management
- **Google OAuth 2.0 Authentication** – Secure login via Google accounts  
- **JWT Authorization** – Protect API endpoints and user data  

### 🗺️ Mapping & Location
- **Interactive Maps** – Built using **Jwag (Leaflet)** to display bike stations  
- **Station Visualization** – See all bike stations and real-time availability  

### 💳 Payments
- **Secure Payment Processing** – Integrated with **Stripe** for rental payments  
- **Rental Billing** – Calculate charges automatically

### ✅ Testing & CI
- **Unit Testing** – JUnit & Mockito for backend logic  
- **Continuous Integration** – GitHub Actions automatically runs tests on push/pull requests


## Project Setup
### Prerequisites
- Java 25
- Node.js & npm
- MongoDB
- Maven
---
### Environment Variables / Configuration

Before running the project, you need to create environment variables or configuration files for both backend and frontend.

Create a `.env` file in the `Backend` folder with the following structure:

```env
STRIPE_SECRET_KEY=your_stripe_secret_key
GOOGLE_CLIENT_ID=your_google_client_id
GOOGLE_CLIENT_SECRET=your_google_client_secret
MONGODB_URI=your_mongodb_connection_uri
```

Create a `.env` file in the `Frontend` folder with the following structure:

```env
REACT_APP_STRIPE_PUBLISHABLE_KEY=your_stripe_publishable_key
REACT_APP_JWAG_API_KEY=your_jwag_api_key
```

## Running the Project 
### Backend Setup
```bash
cd backend
mvn clean install
mvn spring-boot:run
```

### Frontend Setup
```bash
cd frontend
npm install
npm run dev
```
