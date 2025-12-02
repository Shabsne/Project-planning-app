package org.example.projectplanningapp.repositoriyTest;

import org.example.projectplanningapp.models.Employee;
import org.example.projectplanningapp.repositories.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.jupiter.api.Assertions.*;

public class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository repository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setup() {
        jdbcTemplate.update("DELETE FROM Employee");
        jdbcTemplate.update("INSERT INTO Employee (employeeRoleId, name, email, password) VALUES (1,'Test User','test@mail.com','1234')");
    }

    @Test
    void findByEmail_returnsEmployee_whenEmailExists() {
        Employee employee = repository.findByEmail("test@mail.com");

        assertNotNull(employee);
        assertEquals("Test User", employee.getName());
        assertEquals("test@mail.com", employee.getEmail());
    }

    @Test
    void findByEmail_returnsNull_whenEmailDoesNotExist() {
        Employee employee = repository.findByEmail("unknown@mail.com");

        assertNull(employee);
    }
}
