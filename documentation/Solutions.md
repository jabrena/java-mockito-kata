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
