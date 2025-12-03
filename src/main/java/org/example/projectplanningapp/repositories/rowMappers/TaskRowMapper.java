package org.example.projectplanningapp.repositories.rowMappers;


import org.example.projectplanningapp.models.Project;
import org.example.projectplanningapp.models.Status;
import org.example.projectplanningapp.models.Task;
import org.springframework.jdbc.core.RowMapper;


import java.sql.ResultSet;
import java.sql.SQLException;

public class TaskRowMapper implements RowMapper<Task> {

    @Override
    public Task mapRow(ResultSet rs, int rowNum) throws SQLException {
        Task task = new Task();
        task.setId(rs.getInt("taskId"));
        task.setTitle(rs.getString("title"));
        task.setDescription(rs.getString("description"));
        task.setStatus(Status.valueOf(rs.getString("status")));
        task.setEstimatedHours(rs.getInt("estimatedHours"));
        task.setActualHours(rs.getInt("actualHours"));
        task.setDeadline(rs.getTimestamp("deadline").toLocalDateTime());

        // Parent project
        int projectId = rs.getInt("parentProjectId");
        task.setParentProject(new Project(projectId));

        // Parent task (optional)
        try {
            int parentTaskId = rs.getInt("parentTaskId");
            if (!rs.wasNull()) {
                Task parentTask = new Task();
                parentTask.setId(parentTaskId);
                task.setParentTask(parentTask);
            }
        } catch (SQLException ignored) {}

        return task;
    }
}
