package info.jab.demo.service;

import info.jab.demo.repository.Resource1Repository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class Resource1ServiceTest {

    @Mock
    private Resource1Repository resource1Repository;

    @InjectMocks
    private Resource1ServiceImpl resource1Service;

    @Test
    void shouldReturnEmptyListWhenRepositoryReturnsEmptyList() {
        // Given - nothing in the database
        when(resource1Repository.findAll()).thenReturn(Collections.emptyList());

        // When
        List<String> result = resource1Service.getResources();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(resource1Repository, times(1)).findAll();
    }

    @Test
    void shouldReturnResourcesWhenRepositoryReturnsData() {
        // Given - something in the database
        List<String> repositoryData = Arrays.asList("resource1", "resource2", "resource3");
        when(resource1Repository.findAll()).thenReturn(repositoryData);

        // When
        List<String> result = resource1Service.getResources();

        // Then
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("resource1", result.get(0));
        assertEquals("resource2", result.get(1));
        assertEquals("resource3", result.get(2));
        verify(resource1Repository, times(1)).findAll();
    }

    @Test
    void shouldThrowExceptionWhenRepositoryThrowsException() {
        // Given - broken database scenario
        RuntimeException databaseException = new RuntimeException("Database connection failed");
        when(resource1Repository.findAll()).thenThrow(databaseException);

        // When & Then
        RuntimeException thrownException = assertThrows(RuntimeException.class,
                () -> resource1Service.getResources());

        assertEquals("Database connection failed", thrownException.getMessage());
        verify(resource1Repository, times(1)).findAll();
    }
}
