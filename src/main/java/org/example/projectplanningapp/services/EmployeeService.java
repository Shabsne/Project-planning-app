package org.example.projectplanningapp.services;

import org.example.projectplanningapp.models.Employee;
import org.example.projectplanningapp.repositories.EmployeeRepository;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public void registerEmployee(Employee employee) {
        employeeRepository.registerEmployee(employee);
    }

    public boolean emailExists(String email) {
        return employeeRepository.getEmployeeFromEmail(email);
    }
}
