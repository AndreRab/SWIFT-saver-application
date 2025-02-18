# 🌐 SWIFT Saver Application 🚀

Welcome to the **SWIFT Saver Application** – a simple and efficient REST API application designed to work with SWIFT codes. This project is built using **Java** and **Gradle**, following best practices for clean code and easy maintenance.

## 📖 Table of Contents

- [About the Project](#about-the-project)
- [Tech Stack](#tech-stack)
- [API running](#api-running)
- [Testing and Setup](#testing-and-setup)
- [API Endpoints](#api-endpoints)
---

## 🛠️ About the Project

The **SWIFT Saver Application** is a REST API that enables users to manage and retrieve information about SWIFT (Society for Worldwide Interbank Financial Telecommunication) codes. It allows financial institutions to validate, store, and retrieve SWIFT codes with ease.

As an initial database, it uses data from a [csv file](https://github.com/AndreRab/SWIFT-saver-application/blob/master/src/main/resources/Interns_2025_SWIFT_CODES_Sheet1.csv) and then provides access to this data via the API. For quick startup, the project uses Gradle, which automatically runs all tests, resolves dependencies, and starts Docker containers.

---

## ⚙️ Tech Stack

- **Java** (JDK 17+)
- **Gradle** (Build automation)
- **Spring Boot** (REST API development)
- **H2 Database** (In-memory database)
- **JUnit** & **Mockito** (Testing)
- **Docker** (Containerization)

---

## 🚀 API running
This guide explains how to run and stop the entire application.

1. **Install Docker**

    • Go to the [Docker website](https://www.docker.com/products/docker-desktop/) and download Docker Desktop.
   
    • Install Docker Desktop and start the Docker Engine.

2. **Clone the repository**
   ```
   git clone https://github.com/AndreRab/SWIFT-saver-application.git
3. **Navigate to the project directory**
   ```
   cd SWIFT-saver-application
4. **Run an application**
   ```
   ./gradlew deploy 
After the last step, the project begins deploying with tests and application containerization. After a successful deployment, you should wait a few minutes while the Java Spring application starts in the container. After that, you should be able to call the API at http://localhost:8080.

5. **Stop the application**

   ```
   ./gradlew stop 
---

## 🛠️ Testing and Setup
This guide explains how to set up and test the application without running the entire application.
If you haven't already completed the first three steps from the previous section, do so, and then run:

```bash
./gradlew compileAndTest
```
--- 
## 🌐 API Endpoints

📌 SWIFT Code Management

| Method  | Endpoint         | Description                  |
|---------|------------------|------------------------------|
| GET    | /v1/swift-codes/{swift-code}      | Retrieve details of a single SWIFT code whether for a headquarters or branches          |
| GET     | /v1/swift-codes/country/{countryISO2code} | Return all SWIFT codes with details for a specific country     |
| POST     | /v1/swift-codes       |  Adds new SWIFT code entries to the database for a specific country if it hasn't been already created    |
| DELETE  | /v1/swift-codes/{swift-code}  |  Deletes swift-code data if swiftCode matches the one in the database       |
