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
        if (employee.getEmail() == null || employee.getEmail().trim().isEmpty()) {
            throw new ValidationException("Email er påkrævet");
        }

        if (emailExists(employee.getEmail())) {
            throw new ValidationException("En medarbejder med denne email eksisterer allerede");
        }

        //Valider navn
        if (employee.getName() == null || employee.getName().trim().isEmpty()) {
            throw new ValidationException("Navn er påkrævet");
        }

        //Valider password
        if (employee.getPassword() == null || employee.getPassword().trim().isEmpty()) {
            throw new ValidationException("Adgangskode er påkrævet");
        }

        if (employee.getPassword().length() < 4) {
            throw new ValidationException("Adgangskoden skal være mindst 4 tegn");
        }

        employeeRepository.registerEmployee(employee);
    }

    public Employee login(String email, String password) {
        //Valider input
        if (email == null || email.trim().isEmpty()) {
            throw new ValidationException("Email er påkrævet");
        }

        if (password == null || password.trim().isEmpty()) {
            throw new ValidationException("Adgangskode er påkrævet");
        }

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
        //Valider input
        if (employee.getName() == null || employee.getName().trim().isEmpty()) {
            throw new ValidationException("Navn er påkrævet");
        }

        if (employee.getEmail() == null || employee.getEmail().trim().isEmpty()) {
            throw new ValidationException("Email er påkrævet");
        }

        //Tjek at employee eksisterer
        getEmployeeFromId(employee.getEmployeeId());

        employeeRepository.updateOwnProfile(employee);
    }
    public void updateEmployeeRole(int employeeId, int newRoleId) {
        //Valider at employee eksisterer
        getEmployeeFromId(employeeId);

        //Valider rolle ID
        if (newRoleId < 1 || newRoleId > 2) {
            throw new ValidationException("Ugyldig rolle ID");
        }

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
        //Valider at employee eksisterer
        getEmployeeFromId(employeeId);

        //Valider rolle
        if (role == null) {
            throw new ValidationException("Rolle er påkrævet");
        }

        employeeRepository.updateRole(employeeId, role);
    }

    public void deleteEmployee(int employeeId) {
        //Valider at employee eksisterer før sletning
        getEmployeeFromId(employeeId);

        employeeRepository.deleteEmployee(employeeId);
    }
}
