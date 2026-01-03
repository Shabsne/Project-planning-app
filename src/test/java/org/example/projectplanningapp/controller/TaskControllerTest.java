package org.example.projectplanningapp.controller;

import jakarta.servlet.http.HttpSession;
import org.example.projectplanningapp.controllers.TaskController;
import org.example.projectplanningapp.models.Employee;
import org.example.projectplanningapp.models.Task;
import org.example.projectplanningapp.services.EmployeeService;
import org.example.projectplanningapp.services.ProjectService;
import org.example.projectplanningapp.services.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class TaskControllerTest {

    private TaskService taskService;
    private EmployeeService employeeService;
    private ProjectService projectService;
    private TaskController taskController;
    private Model model;
    private HttpSession session;

    @BeforeEach
    void setup() {
        taskService = mock(TaskService.class);
        projectService = mock(ProjectService.class);
        employeeService = mock(EmployeeService.class);

        taskController = new TaskController(taskService, projectService, employeeService);
        model = mock(Model.class);
        session = mock(HttpSession.class);
    }

    @Test
    void testShowTaskDetails_returnsViewAndAddsAttributes() {
// Arrange
        Employee employee = new Employee();
        employee.setEmployeeId(1);

        when(session.getAttribute("employee")).thenReturn(employee);

        Task task = new Task();
        task.setTaskId(5);

        when(taskService.getTaskFromId(1)).thenReturn(task);

        // Act
        String viewName = taskController.showTaskDetails(1, 1, model, session);

        // Assert
        assertEquals("task/taskDetails", viewName);
        verify(model).addAttribute("task", task);
        verify(taskService).getTaskFromId(1);
    }
}
