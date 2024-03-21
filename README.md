# Kino Backend

This is a Java Spring Boot application for managing the kino.

### Prerequisites

- Java 17 or higher
- MySQL

### Setup

#### 1. Clone the repository:
   ```
   git clone https://github.com/AliHMohammad/kino-backend.git
   ```
#### 2. Navigate to the project directory:

    cd kino-backend

#### 3. Update the `src/main/resources/application.properties` file with your MySQL credentials:

    spring.datasource.url=${JDBC_DATABASE_URL}
    spring.datasource.username=${JDBC_USERNAME}
    spring.datasource.password=${JDBC_PASSWORD}

#### 4. Update the `src/main/resources/application.properties` file with your Admin credentials

    app.admin-username=${ADMIN_USERNAME}
    app.admin-email=${ADMIN_EMAIL}
    app.admin-password=${ADMIN_PASSWORD}

#### 5. Update the `src/main/resources/application.properties` file with your TMDB bearer token. 
You can grab your own free token [here](https://developer.themoviedb.org/reference/intro/getting-started) 

    tmdb.api.key=${TMDB_API_KEY}

#### 6. Build the project:

    You can build the project using one of the following methods:

Option 1 - Using Maven:

      mvn clean install

Option 2 - Using mvnw (MacOs & Linux - or Windows with Git Bash or WSL):

    ./mvnw clean install
Option 3 - Using mwvnw.cmd (Windows):

      mvnw.cmd clean install


#### 7. Run the application:

    java -jar target/hogwarts-student-admin-0.0.1-SNAPSHOT.jar

The application should now be running at `http://localhost:8080`.

### Documentation

For more detailed information about the application, please refer to the [JavaDocs for the project](#) and [API documentation](https://kino-api.azurewebsites.net/).

### Build Status (Production branch)

[![Build and deploy JAR app to Azure Web App - kino-api](https://github.com/AliHMohammad/kino-backend/actions/workflows/production_kino-api.yml/badge.svg?branch=production)](https://github.com/AliHMohammad/kino-backend/actions/workflows/production_kino-api.yml)
### Deployment

The application is deployed to Azure Web App. You can access it [here](https://kino-api.azurewebsites.net/).
