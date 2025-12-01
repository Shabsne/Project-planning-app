package org.example.projectplanningapp.repositories;


import org.example.projectplanningapp.models.Task;
import org.example.projectplanningapp.repositories.rowMappers.TaskRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;

@Repository
public class TaskRepository {
    private final JdbcTemplate jdbc;
    private final TaskRowMapper taskRowMapper = new TaskRowMapper();

    public TaskRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public int createTask(Task task) {
        //Keyholder oprettes til at gemme TaksId
        KeyHolder keyholder = new GeneratedKeyHolder();

        //Lambda funktion til at skrive til databasen
        //LAST_INSERT_ID() bruges ikke, da det er MySQL afhængigt
        jdbc.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO Emmployee (" +
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
            task.setId(id.intValue());
            return id.intValue();
        }
        throw new RuntimeException("Failed to generate ID for task");
    }
}
