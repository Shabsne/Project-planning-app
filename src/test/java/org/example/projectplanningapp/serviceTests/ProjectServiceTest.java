package org.example.projectplanningapp.serviceTests;

import org.example.projectplanningapp.models.Project;
import org.example.projectplanningapp.repositories.ProjectRepository;
import org.example.projectplanningapp.services.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


public class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private ProjectService projectService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateProject() {
        Project project = new Project();
        projectService.createProject(project);

        verify(projectRepository, times(1)).createProject(project);
    }

    @Test
    void testGetAllProjects() {
        when(projectRepository.getAllProjects()).thenReturn(List.of(new Project()));

        List<Project> projects = projectService.getAllProjects();

        assertEquals(1, projects.size());
        verify(projectRepository, times(1)).getAllProjects();
    }

}
