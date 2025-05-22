# Mortgage API

Source of interest rates based on 100% of home's value mortgage: https://www.ing.nl/particulier/hypotheek/actuele-hypotheekrente

Assignment for ING Java developer on May/25.

API was made with Spring Boot 3.4.5, Java 21, and OpenAPI documentation.
It runs with Docker and supports multiple environments.

## Environments
There are 3 environments:
- Local: Port 8081
- Staging: Port 8082
- Production: Port 8083

You can change between them by adding the command below in VM Options:
```
-Dspring.profiles.active={environment}
```

## API Documentation
Swagger API documentation AFTER RUNNING THE APPLICATION:
[http://localhost:8081/swagger-ui/index.html]

We can also check status of application with actuator endpoints:
- Health Check: [http://localhost:8081/actuator/health]
- Environment Info: [http://localhost:8081/actuator/env]
- Metrics: [http://localhost:8081/actuator/metrics]

## API Endpoints
Contains 1 Controller with entry point "/api":

### Controller Methods:
- `GET /api/interest-rates`: Get all interest rates
- `POST /api/mortgage-check`: Create a mortgage check

## Running the Application

### Prerequisites
- Maven 3.9+
- Java 21
- Docker (optional)

### Local Development
1. Clone this repo to your machine
2. Via command line, use 'cd' command to go to the folder of the project
3. Execute the command `mvn clean install` - It will generate the jar of the application
4. Execute the command `java -Dspring.profiles.active=local -jar target/mortgage-0.0.1-SNAPSHOT.jar` to run the application

### Using Postman
You can use Postman to try these APIs. Use the collection in `Mortgage.postman_collection.json` file.

### Docker Deployment
1. Use 'cd' command to go to the folder of the project
2. Execute: `mvn clean package`
3. Build docker file:
   ```bash
   docker build -t mortgage-app .
   ```
4. Run dockerfile:
   ```bash
   # Local environment (default)
   docker run -p 8081:8081 mortgage-app

   # Staging environment
   docker run -e "SPRING_PROFILES_ACTIVE=stg" -p 8082:8082 mortgage-api

   # Production environment
   docker run -e "SPRING_PROFILES_ACTIVE=prd" -p 8083:8083 mortgage-api
   ```

## Future Improvements

1. Create more endpoints:
   - Implement API versioning
   - Create user management for mortgage checks
   - Retrieve mortgage by ID/clientId in case client wants to get it back
   - Store rejection messages in the database
   - Calculate maximum loan amount based on annual income

2. Enhanced Features:
   - Integration with ING's interest rates API for automatic updates
   - Message storage for rejected mortgages (Event Sourcing)
   - User authentication and authorization

3. Infrastructure:
   - Kubernetes deployment (Learning lesson)
   - OAuth2 and JWT security
   - Advanced logging strategies (Datadog)