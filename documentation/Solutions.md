# Solutions

Implementation notes and status for the exercises.

## Exercise 1: Controller - Service interaction

**Implementation Status:** ✅ Completed with Spring Boot 3.5.9

The implementation includes:
- `Resource1Controller` with GET `/v1/resource1` endpoint that returns a list
- `Resource1Service` interface and `Resource1ServiceImpl` implementation
- Unit test `Resource1ControllerTest` using `@WebMvcTest` that:
  - Mocks the service using `@MockBean`
  - Tests the endpoint using `MockMvc`
  - Verifies service method calls using `Mockito.verify`
  - Tests both empty list and populated list scenarios
  - Tests exception handling (broken database scenario)

The controller calls the service method `getResources()` which returns a list. The unit test demonstrates how to mock the service and verify the interaction between controller and service.

**Additional Implementation:**
- `GlobalExceptionHandler` - A `@ControllerAdvice` class that handles `RuntimeException` and returns HTTP 500 Internal Server Error, following microservice exception handling patterns.

## Exercise 2: Service implementation

**Implementation Status:** ✅ Completed

The implementation includes:
- `Resource1Repository` interface for data access layer
- `Resource1ServiceImpl` - Service implementation that depends on the repository
- `Resource1ServiceTest` - Unit tests using TDD approach with `@ExtendWith(MockitoExtension.class)` that:
  - Tests "nothing in database" case using `Mockito.when().thenReturn(Collections.emptyList())`
  - Tests "something in database" case using `Mockito.when().thenReturn(data)`
  - Tests "broken database" case using `Mockito.when().thenThrow(RuntimeException)`
  - Uses `@Mock` for the repository and `@InjectMocks` for the service

The service implementation follows TDD principles: tests were written first, then the implementation was created to make them pass. All three scenarios from the exercise are covered.

## Exercise 3: Capture the call

**Implementation Status:** ✅ Completed

The implementation includes:
- `AlbumEntity` - Model class with UUID as primary key (String id field)
- `CreateAlbumRequest` - DTO class for the POST request body containing album name
- `AlbumRepository` interface with `save(AlbumEntity album)` method
- `AlbumService` interface and `AlbumServiceImpl` implementation that:
  - Generates a UUID using `UUID.randomUUID().toString()`
  - Creates an `AlbumEntity` with the generated UUID and request name
  - Calls the repository's `save` method
- `AlbumController` with POST `/v1/albums` endpoint that:
  - Accepts `CreateAlbumRequest` in the request body
  - Calls the service to create the album
  - Returns HTTP 201 Created with the created album entity
- `AlbumServiceTest` - Unit test using `@ExtendWith(MockitoExtension.class)` that:
  - Uses `ArgumentCaptor<AlbumEntity>` to capture the entity passed to the repository
  - Verifies that a valid UUID was generated and set on the entity
  - Verifies that the service calls `save` on the repository with the new UUID
  - Demonstrates how to use `ArgumentCaptor` to inspect method arguments

The key learning point is using `ArgumentCaptor` to verify that the service generates a new UUID and passes it to the repository's `save` method, ensuring the application (not the database) generates the primary key.

## Exercise 4: Live

**Implementation Status:** ✅ Completed

The implementation includes:
- `Application` - Main Spring Boot application class (located at `info.jab.demo.Application`)
- Spring Boot Docker Compose support - Automatically starts PostgreSQL container from `docker-compose.yml`
- Flyway database migrations - Automatically creates tables and loads test data on startup
- Spring Data JDBC - Repository implementation for database access
- REST API endpoints:
  - GET `/v1/albums` - Returns list of all albums
  - POST `/v1/albums` - Creates a new album

**How to run:**
1. Start the application:
   ```bash
   ./mvnw clean spring-boot:run
   ```
2. The application will automatically:
   - Start PostgreSQL container via Docker Compose
   - Run Flyway migrations to create the `albums` table
   - Load test data from migration scripts
   - Start the web server on port 8080

**Testing the API:**
- Get all albums:
  ```bash
  curl -X GET http://localhost:8080/v1/albums
  ```
- Create a new album:
  ```bash
  curl -X POST http://localhost:8080/v1/albums \
    -H "Content-Type: application/json" \
    -d '{"name": "My New Album"}'
  ```

**Key Implementation Details:**
- **Port:** The application runs on port 8080 (default Spring Boot port), not 5705 as mentioned in the exercise
- **Endpoint Path:** `/v1/albums` (includes the `/v1` prefix)
- **Database:** PostgreSQL automatically managed by Spring Boot Docker Compose support
- **Migrations:** Flyway migrations in `src/main/resources/db/migration/`:
  - `V1__Create_albums_table.sql` - Creates the albums table
  - `V2__Insert_test_data.sql` - Inserts 3 test albums
- **Persistence:** Uses custom `insertAlbum()` method with `@Modifying` and `@Query` annotations to properly handle manually assigned UUIDs in Spring Data JDBC

**Note:** The main class is `Application` (not `ThebandApplication` as mentioned in the exercise), and the application uses Spring Boot 3.5.9 with Java 25.
