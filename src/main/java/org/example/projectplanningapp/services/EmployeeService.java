package org.example.projectplanningapp.services;

import org.example.projectplanningapp.models.Employee;
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
        return employeeRepository.getEmployeeFromId(id);
    }
}
