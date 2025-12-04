package org.example.projectplanningapp.repositories.rowMappers;


import org.example.projectplanningapp.models.Status;
import org.example.projectplanningapp.models.Task;
import org.springframework.jdbc.core.RowMapper;


import java.sql.ResultSet;
import java.sql.SQLException;

public class TaskRowMapper implements RowMapper<Task> {

    @Override
    public Task mapRow(ResultSet rs, int rowNum) throws SQLException {
        Task task = new Task();
        task.setTaskId(rs.getInt("taskId"));
        task.setProjectId(rs.getInt("projectId"));
        Integer parentTaskId = rs.getObject("parentTaskId", Integer.class);
        if (parentTaskId != null) {
            Task parentTask = new Task();
            parentTask.setTaskId(parentTaskId);
            task.setParentTask(parentTask);
        }
        task.setTitle(rs.getString("title"));
        task.setDescription(rs.getString("description"));
        task.setStatus(Status.valueOf(rs.getString(
                "status")));

        int estHours = rs.getInt("estimatedHours");
        task.setEstimatedHours(rs.wasNull() ? null : estHours);

        int actHours = rs.getInt("actualHours");
        task.setActualHours(rs.wasNull() ? null : actHours);

        java.sql.Timestamp ts = rs.getTimestamp("deadline");
        task.setDeadline(ts != null ? ts.toLocalDateTime() : null);

        return task;
    }
}
