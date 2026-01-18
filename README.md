# Java Mockito Kata

## How to build in local?

```bash
./mvnw clean test
./mvnw clean spring-boot:run
```

## Testing the API

Once the application is running (on port 8080), you can test the endpoints:

### Get all albums

```bash
curl -X GET http://localhost:8080/v1/albums
```

### Create a new album

```bash
curl -X POST http://localhost:8080/v1/albums \
  -H "Content-Type: application/json" \
  -d '{"name": "My New Album"}'
```

## References

- https://github.com/crispab/theband/tree/master
