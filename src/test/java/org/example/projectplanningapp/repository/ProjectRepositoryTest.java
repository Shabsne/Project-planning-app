package org.example.projectplanningapp.repository;

import org.example.projectplanningapp.models.Project;
import org.example.projectplanningapp.repositories.ProjectRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;


import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
@Sql(scripts = "classpath:h2init.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class ProjectRepositoryTest {

    @Autowired
    private ProjectRepository projectRepository;

    @Test
    void readAllProjects_returnsAllFromDatabase() {
        // When: Hent alle projekter
        List<Project> allProjects = projectRepository.getAllProjects();

        // Debug output (vises i test logs)
        System.out.println("DEBUG: Found " + allProjects.size() + " projects in database");
        allProjects.forEach(p ->
                System.out.println("  - " + p.getName() + " (" + p.getDescription() + ")")
        );

        // Then: Verificer resultater
        assertNotNull(allProjects);
        assertThat(allProjects).hasSize(5); // Tilpas efter hvor mange projekter din H2-init indeholder

        // Tjek specifikke projekter
        Project projectAlpha = allProjects.stream()
                .filter(p -> "Website Redesign".equals(p.getName()))
                .findFirst().orElse(null);
        assertThat(projectAlpha).isNotNull();
        assertThat(projectAlpha.getDescription()).isEqualTo("Redesign af virksomhedens website");

        Project projectBeta = allProjects.stream()
                .filter(p -> "Mobil App".equals(p.getName()))
                .findFirst().orElse(null);
        assertThat(projectBeta).isNotNull();
        assertThat(projectBeta.getDescription()).isEqualTo("Udvikling af mobil app til kunder");
    }
}