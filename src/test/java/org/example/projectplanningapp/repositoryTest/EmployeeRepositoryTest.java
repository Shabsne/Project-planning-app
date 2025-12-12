package org.example.projectplanningapp.repositoryTest;

import org.example.projectplanningapp.models.Employee;
import org.example.projectplanningapp.repositories.EmployeeRepository;
import org.example.projectplanningapp.repositories.rowMappers.EmployeeRowMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import static org.assertj.core.api.Assertions.assertThat;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@SpringBootTest
@ActiveProfiles("test")
@Sql(scripts = "/h2init.sql", executionPhase = BEFORE_TEST_METHOD)
public class EmployeeRepositoryTest {

    @Autowired
    private JdbcTemplate jdbc;
    private EmployeeRepository repo;


    @Mock
    private JdbcTemplate jdbcTemplate;
    private EmployeeRepository employeeRepository;


    @BeforeEach
    void setup() {
        jdbcTemplate = mock(JdbcTemplate.class);
        employeeRepository = new EmployeeRepository(jdbcTemplate);
    }

    @Test
    void findByEmail_returnsEmployee_whenEmailExists() {
        Employee expected = new Employee();
        expected.setEmployeeId(1);
        expected.setName("Test User");
        expected.setEmail("test@mail.com");
        expected.setPassword("1234");

        when(jdbcTemplate.queryForObject(
                anyString(),
                any(EmployeeRowMapper.class),
                eq("test@mail.com")
        )).thenReturn(expected);

        // Act
        Employee employee = employeeRepository.findByEmail("test@mail.com");

        // Assert
        assertNotNull(employee);
        assertEquals("Test User", employee.getName());
        assertEquals("test@mail.com", employee.getEmail());
    }

    @Test
    void findByEmail_returnsNull_whenEmailDoesNotExist() {
        Employee employee = employeeRepository.findByEmail("unknown@mail.com");

        assertNull(employee);
    }

    @Test
    void testGetAllEmployees_returnsAllEmployeesFromDatabase() {
        repo = new EmployeeRepository(jdbc);

        List<Employee> employees = repo.getAllEmployees();

        assertThat(employees).hasSize(9);
        // Check some specific employees from production data
        Employee alice = repo.getEmployeeFromEmail("alice@example.com");
        assertThat(alice).isNotNull();
        assertThat(alice.getName()).isEqualTo("Alice Hansen");
        assertThat(alice.getRole().getDisplayName()).isEqualTo("Admin");

        Employee benjamin = repo.getEmployeeFromEmail("benjamin@example.com");
        assertThat(benjamin).isNotNull();
        assertThat(benjamin.getName()).isEqualTo("Benjamin Larsen");
        assertThat(benjamin.getRole().getDisplayName()).isEqualTo("Udvikler");

        // Check edge case employee
        Employee noRole = repo.getEmployeeFromEmail("test.norole@example.com");
        assertThat(noRole).isNotNull();
        assertThat(noRole.getName()).isEqualTo("Test User Uden Rolle");
        assertThat(noRole.getRole()).isNull();
    }

}
