package org.example.projectplanningapp.serviceTests;

import org.example.projectplanningapp.models.Task;
import org.example.projectplanningapp.repositories.TaskRepository;
import org.example.projectplanningapp.services.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class TaskServiceTest {

    private TaskRepository taskRepository;
    private TaskService taskService;

    @BeforeEach
    void setup() {
        taskRepository = mock(TaskRepository.class);
        taskService = new TaskService(taskRepository);
    }

    @Test
    void testGetTasksForProject() {
        when(taskRepository.getTasksInProject(1)).thenReturn(List.of(new Task()));

        List<Task> tasks = taskService.getTasksInProject(1);

        assertEquals(1, tasks.size());
        verify(taskRepository).getTasksInProject(1);
    }

    @Test
    void testGetTaskById() {
        Task task = new Task();
        when(taskRepository.getTaskFromId(1)).thenReturn(task);

        Task result = taskService.getTaskFromId(1);

        assertNotNull(result);
        verify(taskRepository).getTaskFromId(1);
    }
}
