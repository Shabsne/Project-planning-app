package org.example.projectplanningapp.repositories.rowMappers;


import org.example.projectplanningapp.models.Employee;
import org.example.projectplanningapp.models.Project;
import org.example.projectplanningapp.models.Status;
import org.example.projectplanningapp.models.Task;
import org.springframework.jdbc.core.RowMapper;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class TaskRowMapper implements RowMapper<Task> {

    @Override
    public Task mapRow (ResultSet rs,int rowNum) throws SQLException {
        Task task = new Task();
        task.setId(rs.getInt("id"));
        task.setTitle(rs.getString("title"));
        task.setDescription(rs.getString("description"));
        task.setStatus((Status) rs.getObject("status"));
        task.setEstimatedHours(rs.getInt("estimatedHours"));
        task.setActualHours(rs.getInt("actualHours"));
        task.setDeadline((LocalDateTime) rs.getObject("deadline"));
        task.setParentProject((Project) rs.getObject("parentProject"));
        task.setParentTask((Task) rs.getObject("parentTask"));
        task.setSubTasks((List<Task>) rs.getObject("subTasks"));
        task.setAssignedEmployees((List<Employee>) rs.getObject("assignedEmployees"));
        return task;
    }
}
