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

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AlbumServiceTest {

    @Mock
    private AlbumRepository albumRepository;

    @InjectMocks
    private AlbumServiceImpl albumService;

    @Test
    void shouldCreateAlbumWithGeneratedUUID() {
        // Given
        CreateAlbumRequest request = new CreateAlbumRequest("Test Album");
        AlbumEntity savedAlbum = new AlbumEntity("generated-uuid", "Test Album");
        when(albumRepository.save(any(AlbumEntity.class))).thenReturn(savedAlbum);

        // When
        AlbumEntity result = albumService.createAlbum(request);

        // Then
        assertNotNull(result);
        assertEquals("Test Album", result.name());

        // Use ArgumentCaptor to verify the UUID was generated
        ArgumentCaptor<AlbumEntity> albumCaptor = ArgumentCaptor.forClass(AlbumEntity.class);
        verify(albumRepository).save(albumCaptor.capture());

        AlbumEntity capturedAlbum = albumCaptor.getValue();
        assertNotNull(capturedAlbum.id(), "UUID should be generated");
        assertTrue(isValidUUID(capturedAlbum.id()), "ID should be a valid UUID");
        assertEquals("Test Album", capturedAlbum.name());
    }

    private boolean isValidUUID(String uuid) {
        try {
            UUID.fromString(uuid);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
