package org.example.projectplanningapp.repositories;

import org.example.projectplanningapp.models.Employee;
import org.example.projectplanningapp.repositories.rowMappers.EmployeeRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class EmployeeRepository {
    private final JdbcTemplate jdbc;
    private final EmployeeRowMapper rowMapper = new EmployeeRowMapper();

    public EmployeeRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public void registerEmployee(Employee employee) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbc.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO employees (employeeRoleId, employeeName, email, password)" +
                            "VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS
            );
            ps.setObject(1, employee.getRole());
            ps.setString(2, employee.getName());
            ps.setString(3, employee.getEmail());
            ps.setString(4, employee.getPassword());
            return ps;
        }, keyHolder);

        Number id = keyHolder.getKey();

        if (id != null) {
            employee.setEmployeeId(id.intValue());
        }
    }

    public Employee getEmployeeFromEmail(String email) {
        List<Employee> list = jdbc.query(
                "SELECT * FROM employees WHERE email = ?", rowMapper, email);
        return list.isEmpty() ? null : list.get(0);
    }

    public List<Employee> getAllEmployees() {
        return jdbc.query("SELECT * FROM employees", rowMapper);
    }

    public Employee getEmployeeFromId(int id) {
        List<Employee> list = jdbc.query(
                "SELECT * FROM employees WHERE id = ?", rowMapper, id);
        return list.isEmpty() ? null : list.get(0);
    }

    public void deleteEmployee(int id) {
        jdbc.update("DELETE FROM employees WHERE id = ?", id);
    }
}
