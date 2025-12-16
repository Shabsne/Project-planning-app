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

    public boolean emailExists(String email) {
        return employeeRepository.getEmployeeFromEmail(email) != null;
    }

    public void updateOwnProfile(Employee updatedEmployee) {
        employeeRepository.updateOwnProfile(updatedEmployee);
    }
    public void updateEmployeeRole(int employeeId, int newRoleId) {
        employeeRepository.updateEmployeeRole(employeeId, newRoleId);
    }

    public Employee login(String email, String password) {
        Employee employee = employeeRepository.findByEmail(email);

        if (employee == null) {
            return null;
        }

        if (!employee.getPassword().equals(password)) {
            return null;
        }

        return employee;
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
