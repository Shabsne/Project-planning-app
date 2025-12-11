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
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import jakarta.servlet.http.HttpSession;
import org.example.projectplanningapp.models.Employee;
import org.example.projectplanningapp.models.Role;


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

    @Mock
    private HttpSession session;

    @InjectMocks
    private ProjectController projectController;

    private Project sampleProject;
    private Employee sampleEmployee;

    @BeforeEach
    void setUp() {
        sampleProject = new Project();
        sampleProject.setId(1);
        sampleProject.setName("Test Project");

        sampleEmployee = new Employee();
        sampleEmployee.setEmployeeId(1);
        sampleEmployee.setName("Test User");
        sampleEmployee.setEmail("test@example.com");
        sampleEmployee.setPassword("password");
        sampleEmployee.setRole(Role.EMPLOYEE);
    }

    @Test
    void testListProjects_WithLoggedInUser() {
        // Arrange: Mock session med logged in bruger
        when(session.getAttribute("employee")).thenReturn(sampleEmployee);

        // Mock projectService til at returnere brugerens projekter
        when(projectService.getProjectsForEmployee(1)).thenReturn(List.of(sampleProject));

        // Act: Kald controller-metoden
        String viewName = projectController.listProjects(model, session);

        // Assert: Tjek at den returnerer den rigtige view-navn
        assertEquals("project/list", viewName);

        // Verify: Tjek at model.addAttribute blev kaldt korrekt
        verify(model, times(1)).addAttribute(eq("projects"), any());
        verify(projectService, times(1)).getProjectsForEmployee(1);
    }

    @Test
    void testListProjects_WithoutLoggedInUser_RedirectsToLogin() {
        // Arrange: Mock session uden logged in bruger
        when(session.getAttribute("employee")).thenReturn(null);

        // Act: Kald controller-metoden
        String viewName = projectController.listProjects(model, session);

        // Assert: Tjek at den redirecter til login
        assertEquals("redirect:/", viewName);

        // Verify: Tjek at projectService IKKE blev kaldt
        verify(projectService, never()).getProjectsForEmployee(anyInt());
    }


}