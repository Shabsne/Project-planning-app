package org.example.projectplanningapp.repositories;

import org.example.projectplanningapp.models.Employee;
import org.example.projectplanningapp.models.Status;
import org.example.projectplanningapp.models.Task;
import org.example.projectplanningapp.repositories.rowMappers.EmployeeRowMapper;
import org.example.projectplanningapp.repositories.rowMappers.TaskRowMapper;
import org.example.projectplanningapp.utils.taskComparators.DeadlineComparator;
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
    private final DeadlineComparator deadlineComparator = new DeadlineComparator();

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
            ps.setString(3, task.getTitle() != null ? task.getTitle() : "New Task");
            ps.setString(4, task.getDescription());
            ps.setString(5, task.getStatus() != null ? task.getStatus().name() : Status.TODO.name());
            ps.setObject(6, task.getEstimatedHours(), java.sql.Types.INTEGER);
            ps.setObject(7, task.getActualHours(), java.sql.Types.INTEGER);
            ps.setTimestamp(8, task.getDeadline() != null ? java.sql.Timestamp.valueOf(task.getDeadline()) : null);

            return ps;
        }, keyHolder);

        Number taskId = keyHolder.getKey();
        if (taskId != null) {
            task.setTaskId(taskId.intValue());
        }
    }

    public Task getTaskFromId(int taskId) {
        String sql = "SELECT * FROM Task WHERE taskId = ?";
        List<Task> tasks = jdbc.query(sql, taskRowMapper, taskId);
        return tasks.isEmpty() ? null : tasks.get(0);
    }

    public List<Task> getTasksInProject(int projectId) {
        String sql = "SELECT * FROM Task WHERE projectId = ?";
        List<Task> tasks = jdbc.query(sql, taskRowMapper, projectId);
        return tasks.isEmpty() ? Collections.emptyList() : tasks;
    }

    public void updateTask(Task task) {
        String sql = "UPDATE Task SET projectId=?, parentTaskId=?, title=?, description=?, " +
                "status=?, estimatedHours=?, actualHours=?, deadline=? WHERE taskId=?";

        jdbc.update(sql,
                task.getProjectId(),
                task.getParentTaskId(),
                task.getTitle() != null ? task.getTitle() : "New Task",
                task.getDescription(),
                task.getStatus() != null ? task.getStatus().name() : Status.TODO.name(),
                task.getEstimatedHours(),
                task.getActualHours(),
                task.getDeadline(),
                task.getTaskId()
        );
    }

    public void removeAllAssignedEmployeesFromTask(int taskId) {
        String sql = "DELETE FROM TaskEmployee WHERE taskId = ?";
        jdbc.update(sql, taskId);
    }

    public List<Task> getSubTasks(int parentTaskId) {
        String sql = "SELECT * FROM Task WHERE parentTaskId = ?";
        List<Task> tasks = jdbc.query(sql, taskRowMapper, parentTaskId);
        tasks.sort(deadlineComparator);
        return tasks.isEmpty() ? Collections.emptyList() : tasks;
    }

    public void deleteTask(int taskId) {
        String sql = "DELETE FROM Task WHERE taskId = ?";
        jdbc.update(sql, taskId);
    }

    public List<Task> getNextTasksForEmployee(int employeeId) {
        String sql = """
        SELECT t.*
        FROM Task t
        INNER JOIN TaskEmployee te ON t.taskId = te.taskId
        WHERE te.employeeId = ?
        LIMIT 3
    """;

        List<Task> tasks = jdbc.query(sql, taskRowMapper, employeeId);
        tasks.sort(deadlineComparator);
        return tasks.isEmpty() ? Collections.emptyList() : tasks;
    }



    // Add employee to a task
    public void assignEmployeeToTask(int taskId, int employeeId) {
        String sql = "INSERT IGNORE INTO TaskEmployee (taskId, employeeId) VALUES (?, ?)";
        jdbc.update(sql, taskId, employeeId);
    }

    // Remove employee from a task
    public void removeEmployeeFromTask(int taskId, int employeeId) {
        String sql = "DELETE FROM TaskEmployee WHERE taskId = ? AND employeeId = ?";
        jdbc.update(sql, taskId, employeeId);
    }


    public List<Employee> getAssignedEmployeesForTask(int taskId) {
        String sql = """
        SELECT e.*
        FROM Employee e
        INNER JOIN TaskEmployee te ON e.employeeId = te.employeeId
        WHERE te.taskId = ?
    """;

        return jdbc.query(sql, new EmployeeRowMapper(), taskId);
    }

    public List<Task> getRootTasks(int projectId) {
        String sql = """
                SELECT *
                FROM Task
                WHERE projectId = ?
                AND parentTaskId IS NULL
                """;

        List<Task> tasks = jdbc.query(sql, taskRowMapper, projectId);
        tasks.sort(deadlineComparator);
        return tasks.isEmpty() ? Collections.emptyList() : tasks;
    }

    public List<Task> getAssignedTasksForEmployee(int employeeId) {
        String sql = """
        SELECT t.*
        FROM Task t
        JOIN TaskEmployee te ON t.taskId = te.taskId
        WHERE te.employeeId = ?
        """;

        List<Task> tasks = jdbc.query(sql, taskRowMapper, employeeId);
        tasks.sort(deadlineComparator);

        return tasks.isEmpty() ? Collections.emptyList() : tasks;
    }

    public int countTasksByProjectId(int projectId) {
        String sql = "SELECT COUNT(*) FROM Task WHERE projectId = ?";


        return jdbc.queryForObject(sql, Integer.class, projectId);
    }



}
