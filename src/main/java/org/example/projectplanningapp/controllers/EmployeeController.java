package org.example.projectplanningapp.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.example.projectplanningapp.services.EmployeeService;
import org.springframework.stereotype.Controller;

@Controller
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }


    @GetMapping("/")
    public String showLogInPage() {
        return "logIn";
    }


}
