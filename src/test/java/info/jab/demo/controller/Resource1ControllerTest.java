package info.jab.demo.controller;

import info.jab.demo.service.Resource1Service;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(Resource1Controller.class)
class Resource1ControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private Resource1Service resource1Service;

    @Test
    void shouldReturnEmptyListWhenServiceReturnsEmptyList() throws Exception {
        // Given
        when(resource1Service.getResources()).thenReturn(Collections.emptyList());

        // When & Then
        mockMvc.perform(get("/v1/resource1"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(resource1Service, times(1)).getResources();
    }

    @Test
    void shouldReturnResourcesWhenServiceReturnsData() throws Exception {
        // Given
        List<String> resources = Arrays.asList("resource1", "resource2", "resource3");
        when(resource1Service.getResources()).thenReturn(resources);

        // When & Then
        mockMvc.perform(get("/v1/resource1"))
                .andExpect(status().isOk())
                .andExpect(content().json("[\"resource1\", \"resource2\", \"resource3\"]"));

        verify(resource1Service, times(1)).getResources();
    }

    @Test
    void shouldReturnInternalServerErrorWhenServiceThrowsException() throws Exception {
        // Given - broken database scenario
        RuntimeException databaseException = new RuntimeException("Database connection failed");
        when(resource1Service.getResources()).thenThrow(databaseException);

        // When & Then
        mockMvc.perform(get("/v1/resource1"))
                .andExpect(status().isInternalServerError())
                .andExpect(result -> {
                    Throwable resolvedException = result.getResolvedException();
                    assertNotNull(resolvedException, "Exception should be resolved by exception handler");
                    assertTrue(resolvedException instanceof RuntimeException,
                            "Exception should be RuntimeException");
                    assertEquals("Database connection failed", resolvedException.getMessage());
                });

        verify(resource1Service, times(1)).getResources();
    }
}
