package org.example.projectplanningapp.models;

import java.time.LocalDateTime;
import java.util.List;

public class Task {

    private int id;
    private String title;
    private String description;
    private Status status;
    private int estimatedHours;
    private int actualHours;
    private LocalDateTime deadline;

    private Project parentProject;
    private Task parentTask;
    private List<Task> subTasks;

    private List<Employee> assignedProfiles;




    public Task(){
    }

    public Task(int id, String title, String description, Status status, int estimatedHours, int actualHours, LocalDateTime deadline, Project parentProject, Task parentTask) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.estimatedHours = estimatedHours;
        this.actualHours = actualHours;
        this.deadline = deadline;
        this.parentProject = parentProject;
        this.parentTask = parentTask;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getEstimatedHours() {
        return estimatedHours;
    }

    public void setEstimatedHours(int estimatedHours) {
        this.estimatedHours = estimatedHours;
    }

    public int getActualHours() {
        return actualHours;
    }

    public void setActualHours(int actualHours) {
        this.actualHours = actualHours;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public void setParentProject(Project parentProject) {
        this.parentProject = parentProject;
    }

    public Task getParentTask() {
        return parentTask;
    }

    public void setParentTask(Task parentTask) {
        this.parentTask = parentTask;
    }

    public List<Task> getSubTasks() {
        return subTasks;
    }

    public void setSubTasks(List<Task> subTasks) {
        this.subTasks = subTasks;
    }

    public void addSubTask(Task subTask) {
        subTask.addSubTask(subTask);
        subTask.setParentTask(this);
    }

    public List<Employee> getAssignedProfiles() {
        return assignedProfiles;
    }

    public void setAssignedProfiles(List<Employee> assignedProfiles) {
        this.assignedProfiles = assignedProfiles;
    }

    public void addAssignedProfile(Employee profile) {
        assignedProfiles.add(profile);
    }
}
