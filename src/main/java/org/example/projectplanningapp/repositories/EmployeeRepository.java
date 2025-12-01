package org.example.projectplanningapp.repositories;

import org.example.projectplanningapp.models.Employee;
import org.example.projectplanningapp.repositories.rowMappers.EmployeeRowMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class EmployeeRepository {

    private final JdbcTemplate jdbcTemplate;

    public EmployeeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public Employee findByEmail(String email) {
        String sql = "SELECT * FROM Employee WHERE email = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new EmployeeRowMapper(), email);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }


}
