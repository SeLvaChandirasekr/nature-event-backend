# Nature Event Backend

Phase 1 starter project for the Nature Event application.

## Technology

- Java 21
- Spring Boot 3.4.2
- Maven
- PostgreSQL
- Spring Data JPA
- Spring Web

## PostgreSQL

Create the database:

```sql
CREATE DATABASE nature_event;
```

Then edit:

`src/main/resources/application.properties`

Replace:

```properties
spring.datasource.password=YOUR_POSTGRES_PASSWORD
```

with your PostgreSQL password.

## Run in STS

1. Import as **Existing Maven Project**
2. Right-click project
3. Maven > Update Project
4. Run `NatureEventBackendApplication.java` as Spring Boot App

## Test

GET:

`http://localhost:8080/api/health`

Expected:

`Nature Event Backend is running!`
