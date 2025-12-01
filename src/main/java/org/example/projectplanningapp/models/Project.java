package org.example.projectplanningapp.models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class Project {

    private int id;
    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDateTime endDate;
    private Employee projectLeader;

    private Project parentProject;
    private List<Project> subProjects;

    private List<Task> tasks;
    private List<Employee> assignedEmployees;


    public Project() {}

    public Project(int id, String name, String description, LocalDate startDate, LocalDateTime endDate, Employee projectLeader) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.projectLeader = projectLeader;

    }

    public Project(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public Employee getProjectLeader() {
        return projectLeader;
    }

    public void setProjectLeader(Employee projectLeader) {
        this.projectLeader = projectLeader;
    }

    public Project getParentProject() {
        return parentProject;
    }

    public void setParentProject(Project parentProject) {
        this.parentProject = parentProject;
    }

    public List<Project> getSubProjects() {
        return subProjects;
    }

    public void setSubProjects(List<Project> subProjects) {
        this.subProjects = subProjects;
    }

    public void addSubProject(Project subProject) {
        subProjects.add(subProject);
        subProject.setParentProject(this);
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void addTasks(Task task) {
        tasks.add(task);
    }

    public List<Employee> getAssignedEmployees() {
        return assignedEmployees;
    }

    public void addAssignedEmployees(Employee employee) {
        assignedEmployees.add(employee);
    }
}
