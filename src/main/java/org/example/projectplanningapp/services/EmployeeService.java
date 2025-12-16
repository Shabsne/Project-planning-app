package org.example.projectplanningapp.services;

import org.example.projectplanningapp.exceptions.ResourceNotFoundException;
import org.example.projectplanningapp.exceptions.ValidationException;
import org.example.projectplanningapp.models.Employee;
import org.example.projectplanningapp.models.Role;
import org.example.projectplanningapp.repositories.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public void registerEmployee(Employee employee) {
        //Valider email
        if (emailExists(employee.getEmail())) {
            throw new ValidationException("En medarbejder med denne email eksisterer allerede");
        }

        if (employee.getPassword() == null || employee.getPassword().length() < 4) {
            throw new ValidationException("Adgangskoden skal være mindst 4 tegn");
        }

        employeeRepository.registerEmployee(employee);
    }

    public Employee login(String email, String password) {
        Employee employee = employeeRepository.findByEmail(email);

        if (employee == null) {
            throw new ValidationException("Ugyldig email eller adgangskode");
        }

        if (!employee.getPassword().equals(password)) {
            throw new ValidationException("Ugyldig email eller adgangskode");
        }

        return employee;
    }

    public boolean emailExists(String email) {
        return employeeRepository.getEmployeeFromEmail(email) != null;
    }

    public void updateOwnProfile(Employee employee) {
        //Tjek at employee eksisterer
        getEmployeeFromId(employee.getEmployeeId());
        employeeRepository.updateOwnProfile(employee);
    }
    public void updateEmployeeRole(int employeeId, int newRoleId) {
        employeeRepository.updateEmployeeRole(employeeId, newRoleId);
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.getAllEmployees();
    }

    public Employee getEmployeeFromId(int id) {
        Employee employee = employeeRepository.getEmployeeFromId(id);

        if (employee == null) {
            throw new ResourceNotFoundException("Medarbejder", id);
        }

        return employee;
    }

    public void changeRole(int employeeId, Role role) {
        employeeRepository.updateRole(employeeId, role);
    }

    public void deleteEmployee(int employeeId) {
        employeeRepository.deleteEmployee(employeeId);
    }
}
