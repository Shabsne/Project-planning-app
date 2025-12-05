package org.example.projectplanningapp.controllerTest;

import jakarta.servlet.http.HttpSession;
import org.example.projectplanningapp.controllers.EmployeeController;
import org.example.projectplanningapp.models.Employee;
import org.example.projectplanningapp.services.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class EmployeeControllerTest {

    private EmployeeService employeeService;
    private EmployeeController employeeController;
    private org.springframework.ui.Model model;
    private HttpSession session;


    @BeforeEach
    void setup() {
        employeeService = mock(EmployeeService.class);
        employeeController = new EmployeeController(employeeService);

        model = mock(Model.class);
        session = mock(HttpSession.class);
    }

    @Test
    void loginUser_redirectsToHome_whenLoginSuccessful() {
        Employee employee = new Employee();
        employee.setEmployeeId(1);
        employee.setEmail("test@mail.com");
        employee.setPassword("1234");

        when(employeeService.login("test@mail.com", "1234")).thenReturn(employee);

        String result = employeeController.loginUser("test@mail.com", "1234", model, session);

        assertEquals("redirect:/employee/home/1", result);

        verify(session).setAttribute("employee", employee);
    }
}
