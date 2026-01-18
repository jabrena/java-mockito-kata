package info.jab.demo.service;

import info.jab.demo.controller.CreateAlbumRequest;
import info.jab.demo.model.AlbumEntity;
import info.jab.demo.repository.AlbumRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlbumServiceTest {

    @Mock
    private AlbumRepository albumRepository;

    @InjectMocks
    private AlbumServiceImpl albumService;

    @Test
    void shouldReturnEmptyListWhenRepositoryReturnsEmptyList() {
        // Given - nothing in the database
        when(albumRepository.findAll()).thenReturn(Collections.emptyList());

        // When
        List<AlbumEntity> result = albumService.getAlbums();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(albumRepository, times(1)).findAll();
    }

    @Test
    void shouldReturnAlbumsWhenRepositoryReturnsData() {
        // Given - something in the database
        UUID uuid1 = UUID.randomUUID();
        UUID uuid2 = UUID.randomUUID();
        UUID uuid3 = UUID.randomUUID();
        List<AlbumEntity> repositoryData = Arrays.asList(
                new AlbumEntity(uuid1, "Album 1"),
                new AlbumEntity(uuid2, "Album 2"),
                new AlbumEntity(uuid3, "Album 3")
        );
        when(albumRepository.findAll()).thenReturn(repositoryData);

        // When
        List<AlbumEntity> result = albumService.getAlbums();

        // Then
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(uuid1, result.get(0).id());
        assertEquals("Album 1", result.get(0).name());
        assertEquals(uuid2, result.get(1).id());
        assertEquals("Album 2", result.get(1).name());
        assertEquals(uuid3, result.get(2).id());
        assertEquals("Album 3", result.get(2).name());
        verify(albumRepository, times(1)).findAll();
    }

    @Test
    void shouldThrowExceptionWhenRepositoryThrowsException() {
        // Given - broken database scenario
        RuntimeException databaseException = new RuntimeException("Database connection failed");
        when(albumRepository.findAll()).thenThrow(databaseException);

        // When & Then
        RuntimeException thrownException = assertThrows(RuntimeException.class,
                () -> albumService.getAlbums());

        assertEquals("Database connection failed", thrownException.getMessage());
        verify(albumRepository, times(1)).findAll();
    }

    @Test
    void shouldCreateAlbumWithGeneratedUUID() {
        // Given
        CreateAlbumRequest request = new CreateAlbumRequest("Test Album");
        doNothing().when(albumRepository).insertAlbum(any(UUID.class), anyString());

        // When
        AlbumEntity result = albumService.createAlbum(request);

        // Then
        assertNotNull(result);
        assertEquals("Test Album", result.name());
        assertNotNull(result.id(), "UUID should be generated");
        assertTrue(result.id() instanceof UUID, "ID should be a UUID");

        // Use ArgumentCaptor to verify the UUID was generated and passed to insertAlbum
        ArgumentCaptor<UUID> uuidCaptor = ArgumentCaptor.forClass(UUID.class);
        ArgumentCaptor<String> nameCaptor = ArgumentCaptor.forClass(String.class);
        verify(albumRepository).insertAlbum(uuidCaptor.capture(), nameCaptor.capture());

        UUID capturedUuid = uuidCaptor.getValue();
        String capturedName = nameCaptor.getValue();
        assertNotNull(capturedUuid, "UUID should be generated");
        assertEquals("Test Album", capturedName);
        assertEquals(capturedUuid, result.id(), "Returned album should have the same UUID");
    }
}
