package org.example.projectplanningapp.repositories;


import org.example.projectplanningapp.models.Task;
import org.example.projectplanningapp.repositories.rowMappers.TaskRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.Collections;
import java.util.List;

@Repository
public class TaskRepository {
    private final JdbcTemplate jdbc;
    private final TaskRowMapper taskRowMapper = new TaskRowMapper();

    public TaskRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public void createTask(Task task) {
        //Keyholder oprettes til at gemme TaksId
        KeyHolder keyholder = new GeneratedKeyHolder();

        //Lambda funktion til at skrive til databasen
        jdbc.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO Task (" +
                            "title, description, status," +
                            "estimatedHours, actualHours, deadline," +
                            "parentProject, parentTask, subTasks," +
                            "assignedEmployees) " +
                            "VALUES (?,?,?,?,?,?,?,?,?,?)",
                    PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, task.getTitle());
            ps.setString(2, task.getDescription());
            ps.setObject(3, task.getStatus());
            ps.setInt(4, task.getEstimatedHours());
            ps.setInt(5, task.getActualHours());
            ps.setObject(6, task.getDeadline());
            ps.setObject(7, task.getParentProject());
            ps.setObject(8, task.getParentTask());
            ps.setObject(9, task.getSubTasks());
            ps.setObject(10, task.getAssignedEmployees());
            return ps;
        }, keyholder);

        Number id = keyholder.getKey();
        if (id != null) {
            //LAST_INSERT_ID() bruges ikke, da det er MySQL afhængigt
            task.setId(id.intValue());
        }
    }
    //READS METHODS

    public List<Task> getAllTasks(){
        List <Task> tasks = jdbc.query("SELECT * FROM task",taskRowMapper);
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

    //UPDATE METHOD
    public void updateTask(Task task) {
        String sql = "UPDATE Task SET " +
                "title = ?, " +
                "description = ?, " +
                "status = ?, " +
                "estimatedHours = ?, " +
                "actualHours = ?, " +
                "deadline = ?, " +
                "parentProject = ?, " +
                "parentTask = ?, " +
                "subTasks = ?, " +
                "assignedEmployees = ? " +
                "WHERE taskId = ?";

        jdbc.update(sql,
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getEstimatedHours(),
                task.getActualHours(),
                task.getDeadline(),
                task.getParentProject(),
                task.getParentTask(),
                task.getSubTasks(),
                task.getAssignedEmployees(),
                task.getId()
        );
    }

    public void deleteTask (int taskId){
        String sql = "DELETE FROM Task WHERE taskId = ?";
        jdbc.update(sql,taskId);
    }





}
