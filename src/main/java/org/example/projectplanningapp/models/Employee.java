package org.example.projectplanningapp.models;

import java.util.List;

public class Employee {

    private String name;
    private String email;
    private String password;
    private Role role;
    private int employeeId;

    private List<Project> assignedProjects;
    private List<Task> assignedTasks;

    public Employee() {}
    
   public Employee(String name, String email, String password, Role role, int employeeId) {
       this.name = name;
       this.email = email;
       this.password = password;
       this.role = role;
       this.employeeId = employeeId;

   }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public List<Project> getAssignedProjects() {
        return assignedProjects;
    }

    public void setAssignedProjects(List<Project> assignedProjects) {
        this.assignedProjects = assignedProjects;
    }

    public List<Task> getAssignedTasks() {
        return assignedTasks;
    }

    public void setAssignedTasks(List<Task> assignedTasks) {
        this.assignedTasks = assignedTasks;
    }
}
