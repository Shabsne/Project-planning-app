package org.example.projectplanningapp.models;

import java.util.List;

public class Profile {

    private String name;
    private String email;
    private String password;
    private Role role;
    private int profileId;

    private List<Project> assignedProjects;
    private List<Task> assignedTasks;

    public Profile() {}
    
   public Profile(String name, String email, String password, Role role, int profileId) {
       this.name = name;
       this.email = email;
       this.password = password;
       this.role = role;
       this.profileId = profileId;

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

    public int getProfileId() {
        return profileId;
    }

    public void setProfileId(int profileId) {
        this.profileId = profileId;
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
