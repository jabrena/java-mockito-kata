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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
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
    void shouldCreateAlbum() throws Exception {
        // Given
        CreateAlbumRequest request = new CreateAlbumRequest("Test Album");
        AlbumEntity savedAlbum = new AlbumEntity("test-uuid", "Test Album");
        when(albumService.createAlbum(any(CreateAlbumRequest.class))).thenReturn(savedAlbum);

        // When & Then
        mockMvc.perform(post("/v1/albums")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("test-uuid"))
                .andExpect(jsonPath("$.name").value("Test Album"));

        verify(albumService, org.mockito.Mockito.times(1)).createAlbum(any(CreateAlbumRequest.class));
    }
}
