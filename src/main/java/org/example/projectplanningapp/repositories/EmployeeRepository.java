package org.example.projectplanningapp.repositories;

import org.example.projectplanningapp.models.Employee;
import org.example.projectplanningapp.models.Role;
import org.example.projectplanningapp.repositories.rowMappers.EmployeeRowMapper;
import org.springframework.dao.EmptyResultDataAccessException;
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
                    "INSERT INTO Employee (employeeRoleId, name, email, password)" +
                            "VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS
            );
            ps.setInt(1, employee.getRole().getId());
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

    public void updateOwnProfile(Employee employee) {
        jdbc.update("UPDATE Employee SET name = ?, email = ?, password = ? WHERE employeeId = ?",
                employee.getName(), employee.getEmail(), employee.getPassword(), employee.getEmployeeId());
    }

    public void updateEmployeeRole(int employeeId, int newRoleId) {
        jdbc.update("UPDATE Employee SET employeeRoleId = ? WHERE employeeId = ?", newRoleId, employeeId);
    }

    public Employee getEmployeeFromEmail(String email) {
        List<Employee> list = jdbc.query(
                "SELECT * FROM Employee WHERE email = ?", rowMapper, email);
        return list.isEmpty() ? null : list.get(0);
    }

    public Employee findByEmail(String email) {
        String sql = "SELECT * FROM Employee WHERE email = ?";
        try {
            return jdbc.queryForObject(sql, new EmployeeRowMapper(), email);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<Employee> getAllEmployees() {
        return jdbc.query("SELECT * FROM Employee", rowMapper);
    }

    public Employee getEmployeeFromId(int id) {
        List<Employee> list = jdbc.query(
                "SELECT * FROM Employee WHERE employeeId = ?", rowMapper, id);
        return list.isEmpty() ? null : list.get(0);
    }

    public void deleteEmployee(int id) {
        jdbc.update("DELETE FROM Employee WHERE employeeId = ?", id);
    }

    public void updateRole(int employeeId, Role role) {
        String sql = "UPDATE Employee SET employeeRoleId = ? WHERE employeeId = ?";
        jdbc.update(sql, role.getId(), employeeId);
    }
}
