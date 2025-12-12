package org.example.projectplanningapp.service;

import org.example.projectplanningapp.models.Employee;
import org.example.projectplanningapp.repositories.EmployeeRepository;
import org.example.projectplanningapp.services.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EmployeeServiceTest {


    private EmployeeRepository employeeRepository;
    private EmployeeService employeeService;

    @BeforeEach
    void setup() {
        employeeRepository = mock(EmployeeRepository.class);
        employeeService = new EmployeeService(employeeRepository);
    }

    @Test
    void login_returnsEmployee_whenEmailAndPasswordMatch() {
        Employee employee = new Employee();
        employee.setEmployeeId(1);
        employee.setEmail("test@mail.com");
        employee.setPassword("1234");

        when(employeeRepository.findByEmail("test@mail.com")).thenReturn(employee);

        Employee result = employeeService.login("test@mail.com", "1234");

        assertNotNull(result);
        assertEquals(1, result.getEmployeeId());
    }


    @Test
    void login_returnNull_whenPasswordWrong() {
        Employee employee = new Employee();
        employee.setPassword("1234");

        when(employeeRepository.findByEmail("test@mail.com")).thenReturn(employee);

        Employee result = employeeService.login("test@mail.com", "wrong");

        assertNull(result);
    }
}
