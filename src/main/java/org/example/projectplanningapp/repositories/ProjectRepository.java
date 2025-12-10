package org.example.projectplanningapp.repositories;
import org.example.projectplanningapp.models.Employee;
import org.example.projectplanningapp.models.Project;
import org.example.projectplanningapp.models.Task;
import org.example.projectplanningapp.repositories.rowMappers.EmployeeRowMapper;
import org.example.projectplanningapp.repositories.rowMappers.ProjectRowMapper;
import org.example.projectplanningapp.repositories.rowMappers.TaskRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

@Repository
public class ProjectRepository {

    private final JdbcTemplate jdbcTemplate;

    public ProjectRepository(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;

    }

    public void createProject(Project project) {
        String sql = "INSERT INTO Project (name, description, startDate, endDate, parentProjectId, projectLeaderId) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(sql,
                project.getName(),
                project.getDescription(),
                project.getStartDate() != null ? java.sql.Date.valueOf(project.getStartDate()) : null,
                project.getEndDate() != null ? java.sql.Timestamp.valueOf(project.getEndDate()) : null,
                project.getParentProject() != null ? project.getParentProject().getId() : null,
                project.getProjectLeader() != null ? project.getProjectLeader().getEmployeeId() : null
        );
    }

    // Se projekter
    public List<Project> getAllProjects() {
        String sql = "SELECT * FROM Project";
        return jdbcTemplate.query(sql, new ProjectRowMapper());
    }

    // Slet projekt
    public void deleteProjectById(int id) {
        String sql = "DELETE FROM Project WHERE projectId = ?";
        jdbcTemplate.update(sql, id);
    }

    // Rediger projekt
    public Project findProjectById(int id) {
        String sql = "SELECT * FROM Project WHERE projectId = ?";
        return jdbcTemplate.queryForObject(sql, new ProjectRowMapper(), id);
    }

    // Efter redigering - Gem
    public void update(Project project) {
        String sql = "UPDATE Project SET name = ?, description = ?, startDate = ?, endDate = ?, projectLeaderId = ? WHERE projectId = ?";

        jdbcTemplate.update(sql,
                project.getName(),
                project.getDescription(),
                project.getStartDate() != null ? Date.valueOf(project.getStartDate()) : null,
                project.getEndDate() != null ? Timestamp.valueOf(project.getEndDate()) : null,
                project.getProjectLeader() != null ? project.getProjectLeader().getEmployeeId() : null,
                project.getId()
        );
    }


    public List<Project> findSubprojects(int parentId) {
        String sql = "SELECT * FROM Project WHERE parentProjectId = ?";
        return jdbcTemplate.query(sql, new ProjectRowMapper(), parentId);
    }

    public List<Task> findTasksByProject(int projectId) {
        String sql = "SELECT * FROM Task WHERE projectId = ?";
        return jdbcTemplate.query(sql, new TaskRowMapper(), projectId);
    }

    public List<Employee> findEmployeesByProject(int projectId) {
        String sql =
                "SELECT e.* FROM Employee e " +
                        "JOIN ProjectEmployee pe ON e.employeeId = pe.employeeId " +
                        "WHERE pe.projectId = ?";

        return jdbcTemplate.query(sql, new EmployeeRowMapper(), projectId);
    }

    public List<Employee> getAvailableEmployeesForTask(int projectId, int taskId) {
        String sql = """
        SELECT e.*
        FROM Employee e
        INNER JOIN ProjectEmployee pe ON e.employeeId = pe.employeeId
        WHERE pe.projectId = ?
          AND e.employeeId NOT IN (
              SELECT te.employeeId
              FROM TaskEmployee te
              WHERE te.taskId = ?
          )
    """;

        return jdbcTemplate.query(sql, new EmployeeRowMapper(), projectId, taskId);
    }

    public void assignEmployeeToProject(int projectId, int employeeId) {
        String sql = "INSERT IGNORE INTO ProjectEmployee (projectId, employeeId) VALUES (?, ?)";
        jdbcTemplate.update(sql, projectId, employeeId);
    }

    public void removeEmployeeFromProject(int projectId, int employeeId) {
        String sql = "DELETE FROM ProjectEmployee WHERE projectId = ? AND employeeId = ?";
        jdbcTemplate.update(sql, projectId, employeeId);
    }

    public List<Employee> getAvailableEmployeesForProject(int projectId) {
        String sql = """
        SELECT e.*
        FROM Employee e
        WHERE e.employeeId NOT IN (
            SELECT pe.employeeId
            FROM ProjectEmployee pe
            WHERE pe.projectId = ?
        )
    """;

        return jdbcTemplate.query(sql, new EmployeeRowMapper(), projectId);
    }
}