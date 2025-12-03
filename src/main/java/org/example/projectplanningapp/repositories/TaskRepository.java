package org.example.projectplanningapp.repositories;


import org.example.projectplanningapp.models.Task;
import org.example.projectplanningapp.repositories.rowMappers.TaskRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.*;

@Repository
public class TaskRepository {
    private final JdbcTemplate jdbc;
    private final TaskRowMapper taskRowMapper = new TaskRowMapper();

    public TaskRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public void createTask(Task task) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbc.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO Task (" +
                            "projectId, parentTaskId, title, description, status, " +
                            "estimatedHours, actualHours, deadline) " +
                            "VALUES (?,?,?,?,?,?,?,?)",
                    PreparedStatement.RETURN_GENERATED_KEYS);


            ps.setInt(1, task.getProjectId());
            ps.setObject(2, task.getParentTaskId() != null ? task.getParentTaskId() : null, java.sql.Types.INTEGER);
            ps.setString(3, task.getTitle());
            ps.setString(4, task.getDescription());
            ps.setString(5, task.getStatus() != null ? task.getStatus().toString(): null);
            ps.setObject(6, task.getEstimatedHours() != null ? task.getEstimatedHours() : null, java.sql.Types.INTEGER);
            ps.setObject(7, task.getActualHours() != null ? task.getActualHours() : null, java.sql.Types.INTEGER);
            ps.setTimestamp(8, task.getDeadline() != null ? java.sql.Timestamp.valueOf(task.getDeadline()) : null);

            return ps;
        }, keyHolder);

        // hent genereret taskId
        Number id = keyHolder.getKey();
        if (id != null) {
            task.setTaskId(id.intValue());
        }
    }


    public List<Task> getAllTasks(){
        List <Task> tasks = jdbc.query("SELECT * FROM Task",taskRowMapper);
        return tasks.isEmpty() ? Collections.emptyList() : tasks;
    }

    public Task getTaskFromId (int taskId){
        String sql = "SELECT * FROM Task WHERE taskId = ?";
        List<Task> tasks = jdbc.query(sql,taskRowMapper,taskId);
        return tasks.isEmpty() ? null : tasks.getFirst();
    }

    public List<Task> getTasksInProject (int projectId){
        String sql = "SELECT * FROM Task WHERE projectId = ?";
        List<Task> tasks = jdbc.query(sql,taskRowMapper,projectId);
        return tasks.isEmpty() ? Collections.emptyList() : tasks;
    }

    public List<Task> getAssignedTasksForEmployee (int employeeId) {
        String sql = "SELECT * FROM Task WHERE employeeId = ?";
        List<Task> list = jdbc.query(sql, taskRowMapper, employeeId);
        return list.isEmpty() ? Collections.emptyList() : list;
    }

    public List<Task> getSubTasks(int parentTaskId) {
        String sql = "SELECT * FROM Task WHERE parentTaskId = ?";
        List<Task> tasks = jdbc.query(sql, taskRowMapper, parentTaskId);
        return tasks.isEmpty() ? Collections.emptyList() : tasks;
    }


    public void updateTask(Task task) {
        String sql = "UPDATE Task SET " +
                "projectId = ?, " +
                "parentTaskId = ?, " +
                "title = ?, " +
                "description = ?, " +
                "status = ?, " +
                "estimatedHours = ?, " +
                "actualHours = ?, " +
                "deadline = ? " +
                "WHERE taskId = ?";

        jdbc.update(sql,
                task.getProjectId(),
                task.getParentTaskId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getEstimatedHours(),
                task.getActualHours(),
                task.getDeadline(),
                task.getTaskId()
        );
    }


    public void deleteTask (int taskId){
        String sql = "DELETE FROM Task WHERE taskId = ?";
        jdbc.update(sql,taskId);
    }



}
