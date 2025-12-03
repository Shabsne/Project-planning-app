package org.example.projectplanningapp.repositoriyTest;

import org.example.projectplanningapp.models.Employee;
import org.example.projectplanningapp.repositories.EmployeeRepository;
import org.example.projectplanningapp.repositories.rowMappers.EmployeeRowMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EmployeeRepositoryTest {

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
}
