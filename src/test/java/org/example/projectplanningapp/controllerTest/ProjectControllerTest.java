package org.example.projectplanningapp.controllerTest;

import org.example.projectplanningapp.controllers.ProjectController;
import org.example.projectplanningapp.models.Project;
import org.example.projectplanningapp.services.EmployeeService;
import org.example.projectplanningapp.services.ProjectService;
import org.example.projectplanningapp.services.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectControllerTest {

    @Mock
    private ProjectService projectService;

    @Mock
    private EmployeeService employeeService;

    @Mock
    private TaskService taskService;

    @Mock
    private Model model;

    @InjectMocks
    private ProjectController projectController;

    private Project sampleProject;

    @BeforeEach
    void setUp() {
        sampleProject = new Project();
        sampleProject.setId(1);
        sampleProject.setName("Test Project");
    }

    @Test
    void testListProjects() {

        // Arrange: Definer hvad projectService skal returnere
        when(projectService.getAllProjects()).thenReturn(List.of(sampleProject));

        // Act: Kald controller-metoden
        String viewName = projectController.listProjects(model);

        // Assert: Tjek at den returnerer den rigtige view-navn
        assertEquals("project/list", viewName);

        // Verify: Tjek at model.addAttribute blev kaldt korrekt
        verify(model, times(1)).addAttribute(eq("projects"), any());
        verify(projectService, times(1)).getAllProjects();
    }
}
