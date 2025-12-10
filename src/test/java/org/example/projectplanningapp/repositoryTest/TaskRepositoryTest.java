package org.example.projectplanningapp.repositoryTest;

import org.example.projectplanningapp.models.Task;
import org.example.projectplanningapp.repositories.TaskRepository;
import org.example.projectplanningapp.repositories.rowMappers.TaskRowMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class TaskRepositoryTest {

    private JdbcTemplate jdbcTemplate;
    private TaskRepository taskRepository;

    @BeforeEach
    void setup() {
        jdbcTemplate = mock(JdbcTemplate.class);
        taskRepository = new TaskRepository(jdbcTemplate);
    }

    @Test
    void testGetTaskById_returnsTask() {

        Task expected = new Task();
        expected.setTaskId(5);
        expected.setTitle("Test task");

        when(jdbcTemplate.queryForObject(
                anyString(),
                any(TaskRowMapper.class),
                eq(1),
                eq(5)
        )).thenReturn(expected);

        Task result = taskRepository.getTaskFromId(5);

        assertNotNull(result);
        assertEquals(5, result.getTaskId());
        assertEquals("Test task", result.getTitle());
    }

    @Test
    void testGetTasksForProject_returnsList() {

        when(jdbcTemplate.query(
                anyString(),
                any(TaskRowMapper.class),
                eq(1)
        )).thenReturn(List.of(new Task()));

        List<Task> tasks = taskRepository.getTasksInProject(1);

        assertEquals(1, tasks.size());
    }
}
