package info.jab.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import info.jab.demo.model.AlbumEntity;
import info.jab.demo.service.AlbumService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AlbumController.class)
class AlbumControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AlbumService albumService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnEmptyListWhenServiceReturnsEmptyList() throws Exception {
        // Given
        when(albumService.getAlbums()).thenReturn(Collections.emptyList());

        // When & Then
        mockMvc.perform(get("/v1/albums"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(albumService, times(1)).getAlbums();
    }

    @Test
    void shouldReturnAlbumsWhenServiceReturnsData() throws Exception {
        // Given
        UUID uuid1 = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
        UUID uuid2 = UUID.fromString("550e8400-e29b-41d4-a716-446655440001");
        UUID uuid3 = UUID.fromString("550e8400-e29b-41d4-a716-446655440002");
        List<AlbumEntity> albums = Arrays.asList(
                new AlbumEntity(uuid1, "Album 1"),
                new AlbumEntity(uuid2, "Album 2"),
                new AlbumEntity(uuid3, "Album 3")
        );
        when(albumService.getAlbums()).thenReturn(albums);

        // When & Then
        mockMvc.perform(get("/v1/albums"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("550e8400-e29b-41d4-a716-446655440000"))
                .andExpect(jsonPath("$[0].name").value("Album 1"))
                .andExpect(jsonPath("$[1].id").value("550e8400-e29b-41d4-a716-446655440001"))
                .andExpect(jsonPath("$[1].name").value("Album 2"))
                .andExpect(jsonPath("$[2].id").value("550e8400-e29b-41d4-a716-446655440002"))
                .andExpect(jsonPath("$[2].name").value("Album 3"));

        verify(albumService, times(1)).getAlbums();
    }

    @Test
    void shouldReturnInternalServerErrorWhenServiceThrowsException() throws Exception {
        // Given - broken database scenario
        RuntimeException databaseException = new RuntimeException("Database connection failed");
        when(albumService.getAlbums()).thenThrow(databaseException);

        // When & Then
        mockMvc.perform(get("/v1/albums"))
                .andExpect(status().isInternalServerError())
                .andExpect(result -> {
                    Throwable resolvedException = result.getResolvedException();
                    assertNotNull(resolvedException, "Exception should be resolved by exception handler");
                    assertTrue(resolvedException instanceof RuntimeException,
                            "Exception should be RuntimeException");
                    assertEquals("Database connection failed", resolvedException.getMessage());
                });

        verify(albumService, times(1)).getAlbums();
    }

    @Test
    void shouldCreateAlbum() throws Exception {
        // Given
        CreateAlbumRequest request = new CreateAlbumRequest("Test Album");
        UUID testUuid = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
        AlbumEntity savedAlbum = new AlbumEntity(testUuid, "Test Album");
        when(albumService.createAlbum(any(CreateAlbumRequest.class))).thenReturn(savedAlbum);

        // When & Then
        mockMvc.perform(post("/v1/albums")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("550e8400-e29b-41d4-a716-446655440000"))
                .andExpect(jsonPath("$.name").value("Test Album"));

        verify(albumService, org.mockito.Mockito.times(1)).createAlbum(any(CreateAlbumRequest.class));
    }
}
