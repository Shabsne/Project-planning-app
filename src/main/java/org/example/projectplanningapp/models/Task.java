package org.example.projectplanningapp.models;

import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Task {

    private int taskId;
    private int projectId;
    private Integer parentTaskId; // Nullable i databasen
    private Task parentTask;

    private String title;
    private String description;
    private Status status;

    // Til Thymeleaf checkbox binding
    private List<Integer> assignedEmployeeIds = new ArrayList<>(); // <-- modtager fra form
    private List<Employee> assignedEmployees = new ArrayList<>();   // <-- konverterer i controller



    private Integer estimatedHours;
    private Integer actualHours;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime deadline;

    private List<Task> subTasks = new ArrayList<>();

    // Constructors
    public Task() {}

    public Task(int taskId, int projectId, Integer parentTaskId, Task parentTask,
                String title, String description, Status status,
                List<Employee> assignedEmployees,
                Integer estimatedHours, Integer actualHours, LocalDateTime deadline,
                List<Task> subTasks) {
        this.taskId = taskId;
        this.projectId = projectId;
        this.parentTaskId = parentTaskId;
        this.parentTask = parentTask;
        this.title = title;
        this.description = description;
        this.status = status;
        this.assignedEmployees = assignedEmployees;
        this.estimatedHours = estimatedHours;
        this.actualHours = actualHours;
        this.deadline = deadline;
        this.subTasks = subTasks;
    }

    // Getters & Setters
    public int getTaskId() { return taskId; }
    public void setTaskId(int taskId) { this.taskId = taskId; }

    public int getProjectId() { return projectId; }
    public void setProjectId(int projectId) { this.projectId = projectId; }

    public Integer getParentTaskId() { return parentTaskId; }
    public void setParentTaskId(Integer parentTaskId) { this.parentTaskId = parentTaskId; }

    public Task getParentTask() { return parentTask; }
    public void setParentTask(Task parent) {
        this.parentTask = parent;
        this.parentTaskId = parent != null ? parent.getTaskId() : null;
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public Integer getEstimatedHours() { return estimatedHours; }
    public void setEstimatedHours(Integer estimatedHours) { this.estimatedHours = estimatedHours; }

    public Integer getActualHours() { return actualHours; }
    public void setActualHours(Integer actualHours) { this.actualHours = actualHours; }

    public LocalDateTime getDeadline() { return deadline; }
    public void setDeadline(LocalDateTime deadline) { this.deadline = deadline; }

    public List<Employee> getAssignedEmployees() { return assignedEmployees; }
    public void setAssignedEmployees(List<Employee> assignedEmployees) { this.assignedEmployees = assignedEmployees; }


    public List<Task> getSubTasks() { return subTasks; }
    public void setSubTasks(List<Task> subTasks) { this.subTasks = subTasks; }

    public List<Integer> getAssignedEmployeeIds() {
        return assignedEmployeeIds;
    }

    public void setAssignedEmployeeIds(List<Integer> assignedEmployeeIds) {
        this.assignedEmployeeIds = assignedEmployeeIds;
    }
}
