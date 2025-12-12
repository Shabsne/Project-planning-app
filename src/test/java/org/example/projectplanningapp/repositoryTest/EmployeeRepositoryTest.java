package org.example.projectplanningapp.repositoryTest;

import org.example.projectplanningapp.models.Employee;
import org.example.projectplanningapp.repositories.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;


import java.util.List;


import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@SpringBootTest
@ActiveProfiles("test")
@Sql(scripts = "classpath:h2init.sql", executionPhase = BEFORE_TEST_METHOD)
public class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository repo;


    @Test
    void readAllEmployees_returnsAllFromDatabase() {
        // When: Hent alle employees
        List<Employee> allEmployees = repo.getAllEmployees();

        // Debug output (vises i test logs)
        System.out.println("DEBUG: Found " + allEmployees.size() + " employees in database");
        allEmployees.forEach(e ->
                System.out.println("  - " + e.getName() + " (" + e.getEmail() + ")")
        );

        // Then: Verificer resultater
        assertThat(allEmployees).isNotNull();
        assertThat(allEmployees).hasSize(9); // 6 originale + 3 edge cases

        // Tjek specifikke employees
        Employee alice = repo.findByEmail("alice@example.com");
        assertThat(alice).isNotNull();
        assertThat(alice.getName()).isEqualTo("Alice Hansen");
        assertThat(alice.getRole().getDisplayName()).isEqualTo("Admin");

        Employee benjamin = repo.findByEmail("benjamin@example.com");
        assertThat(benjamin).isNotNull();
        assertThat(benjamin.getName()).isEqualTo("Benjamin Larsen");
        assertThat(benjamin.getRole().getDisplayName()).isEqualTo("Udvikler");
    }

}
