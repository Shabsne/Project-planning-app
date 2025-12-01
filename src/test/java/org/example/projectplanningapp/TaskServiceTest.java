package org.example.projectplanningapp;

import org.example.projectplanningapp.models.Task;
import org.example.projectplanningapp.models.Project;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class TaskTest {

    @Test
    void testTaskConstructorAndGetters() {
        Project project = new Project(1, "New Project");
        Task subTask = new Task(2, "Subtask", "Do something", "OPEN", 2, 0,
                LocalDateTime.now().plusDays(1), project, null, null, null);

        Task task = new Task(1,
                "Main Task",
                "Main description",
                "IN_PROGRESS",
                5,
                1,
                LocalDateTime.now().plusDays(3),
                project,
                null,
                List.of(subTask),
                List.of("employee1", "employee2"));

        assertEquals(1, task.getId());
        assertEquals("Main Task", task.getTitle());
        assertEquals("Main description", task.getDescription());
        assertEquals("IN_PROGRESS", task.getStatus());
        assertEquals(5, task.getEstimatedHours());
        assertEquals(1, task.getActualHours());
        assertEquals(project, task.getParentProject());
        assertEquals(subTask, task.getSubTasks().get(0));
        assertEquals("employee1", task.getAssignedEmployees().get(0));
        assertEquals("employee2", task.getAssignedEmployees().get(1));
    }
}
