package org.example.projectplanningapp.repository;

import org.example.projectplanningapp.models.Project;
import org.example.projectplanningapp.repositories.ProjectRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY) // Brug H2 in-memory
@Transactional // Ruller tilbage efter hver test
public class ProjectRepositoryTest {

    @Autowired
    private ProjectRepository projectRepository;

    @Test
    void testCreateAndGetProject() {
        Project project = new Project();
        project.setName("Test Project");
        project.setDescription("H2 test project");
        project.setStartDate(LocalDate.now());

        // Gem projekt i H2
        projectRepository.createProject(project);

        // Hent alle projekter
        List<Project> projects = projectRepository.getAllProjects();

        assertEquals(1, projects.size());
        assertEquals("Test Project", projects.get(0).getName());
    }
}