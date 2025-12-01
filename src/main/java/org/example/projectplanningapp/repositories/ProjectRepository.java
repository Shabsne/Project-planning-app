package org.example.projectplanningapp.repositories;
import org.example.projectplanningapp.models.Project;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

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


}