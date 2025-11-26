package org.example.projectplanningapp.repositories.rowMappers;

import org.example.projectplanningapp.models.Employee;
import org.example.projectplanningapp.models.Role;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EmployeeRowMapper implements RowMapper<Employee> {

    @Override
    public Employee mapRow(ResultSet rs, int rowNum) throws SQLException { //midlertidig exception?
        Employee employee = new Employee();
        employee.setEmployeeId(rs.getInt("employeeId"));
        employee.setRole(Role.valueOf(rs.getString("role"))); //Konverterer database-string (fx. "ADMIN") til en Role-enum værdi
        employee.setName(rs.getString("name"));
        employee.setEmail(rs.getString("email"));
        employee.setPassword(rs.getString("password"));
        return employee;
    }
}
