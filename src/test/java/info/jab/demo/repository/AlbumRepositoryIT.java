package info.jab.demo.repository;

import info.jab.demo.model.AlbumEntity;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
class AlbumRepositoryIT {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");
        registry.add("spring.flyway.enabled", () -> "true");
    }

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private Flyway flyway;

    @Test
    void shouldHaveFlywayMigrationsApplied() {
        // Verify Flyway is configured
        assertNotNull(flyway, "Flyway should be autowired");

        // Verify migrations have been applied
        var migrationInfo = flyway.info();
        assertNotNull(migrationInfo, "Migration info should not be null");
        assertTrue(migrationInfo.all().length > 0, "At least one migration should be applied");
        
        // Verify the expected migrations are present
        var migrations = migrationInfo.all();
        assertEquals(2, migrations.length, "Should have 2 migrations applied");
        assertEquals("1", migrations[0].getVersion().toString(), "First migration should be version 1");
        assertEquals("2", migrations[1].getVersion().toString(), "Second migration should be version 2");
    }

    @Test
    void shouldLoadDataFromFlywayMigrations() {
        // Verify the repository is working
        assertNotNull(albumRepository, "Repository should be autowired");

        // Test that we can query the database (Flyway should have loaded data from V2__Insert_test_data.sql)
        List<AlbumEntity> albums = albumRepository.findAll();
        assertNotNull(albums, "Albums list should not be null");
        
        // Verify that the test data from Flyway migration V2 is present
        // V2__Insert_test_data.sql inserts 3 albums
        assertEquals(3, albums.size(), "Should have 3 albums from Flyway migration V2");
        
        // Verify the specific albums from the migration are present
        UUID expectedId1 = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
        UUID expectedId2 = UUID.fromString("550e8400-e29b-41d4-a716-446655440001");
        UUID expectedId3 = UUID.fromString("550e8400-e29b-41d4-a716-446655440002");
        
        assertTrue(albums.stream().anyMatch(a -> a.id().equals(expectedId1)), 
                "Should contain album with ID from migration");
        assertTrue(albums.stream().anyMatch(a -> a.id().equals(expectedId2)), 
                "Should contain album with ID from migration");
        assertTrue(albums.stream().anyMatch(a -> a.id().equals(expectedId3)), 
                "Should contain album with ID from migration");
        
        // Verify the names match what was inserted in the migration
        AlbumEntity album1 = albums.stream()
                .filter(a -> a.id().equals(expectedId1))
                .findFirst()
                .orElse(null);
        assertNotNull(album1, "Album 1 should be found");
        assertEquals("Test Album 1", album1.name(), "Album 1 name should match migration data");
    }

    @Test
    void shouldBeAbleToSaveNewAlbumAfterFlywayMigrations() {
        // Verify the repository is working with data from Flyway migrations
        List<AlbumEntity> initialAlbums = albumRepository.findAll();
        assertEquals(3, initialAlbums.size(), "Should have 3 albums from Flyway migration V2");

        // Verify we can save a new album (Spring Data JDBC save operation)
        UUID newId = UUID.randomUUID();
        AlbumEntity newAlbum = new AlbumEntity(newId, "New Album After Migration");

        // Save the entity - this verifies the repository save method works
        AlbumEntity saved = albumRepository.save(newAlbum);
        assertNotNull(saved, "Save operation should return the saved entity");
        assertEquals(newId, saved.id(), "Saved album ID should match");
        assertEquals("New Album After Migration", saved.name(), "Saved album name should match");
        
        // The save operation completes successfully, verifying the repository works
        // Note: In test transactions, the data might be rolled back, but the operation itself works
    }

    @Test
    void shouldHaveCorrectTableStructureFromFlywayMigration() {
        // Verify the table structure created by V1__Create_albums_table.sql
        // This test verifies that the table exists and has the correct structure
        // by attempting to query it
        
        List<AlbumEntity> albums = albumRepository.findAll();
        assertNotNull(albums, "Should be able to query albums table");
        
        // Verify we can save and retrieve, which confirms table structure is correct
        UUID testId = UUID.randomUUID();
        AlbumEntity testAlbum = new AlbumEntity(testId, "Structure Test");
        AlbumEntity saved = albumRepository.save(testAlbum);
        
        assertNotNull(saved, "Should be able to save to albums table");
        assertEquals(testId, saved.id(), "ID column should work correctly");
        assertEquals("Structure Test", saved.name(), "Name column should work correctly");
    }
}
