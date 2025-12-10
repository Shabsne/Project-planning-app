package org.example.projectplanningapp.repositories.rowMappers;

import org.example.projectplanningapp.models.Employee;
import org.example.projectplanningapp.models.Role;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class EmployeeRowMapper implements RowMapper<Employee> {

    @Override
    public Employee mapRow(ResultSet rs, int rowNum) throws SQLException {
        Employee employee = new Employee();

        employee.setEmployeeId(rs.getInt("employeeId"));
        employee.setName(rs.getString("name"));
        employee.setEmail(rs.getString("email"));
        employee.setPassword(rs.getString("password"));
        employee.setRole(Role.fromId(rs.getInt("employeeRoleId")));

        employee.setAssignedProjects(new ArrayList<>());
        employee.setAssignedTasks(new ArrayList<>());


        return employee;
    }


}
