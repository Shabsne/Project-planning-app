package org.example.projectplanningapp.services;

import org.example.projectplanningapp.models.Employee;
import org.example.projectplanningapp.models.Project;
import org.example.projectplanningapp.repositories.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    private ProjectRepository repository;

    public ProjectService(ProjectRepository repository) {
        this.repository = repository;
    }

    public void createProject(Project project) {
        repository.createProject(project);
    }

    public List<Project> getAllProjects() {
        return repository.getAllProjects();
    }

    public void deleteById(int id) {
        repository.deleteProjectById(id);
    }

    public Project findById(int id) {
        return repository.findProjectById(id);
    }

    public void updateProject(Project project) {
        repository.update(project);
    }

    public List<Employee> getProjectEmployees(int projectId){
        return repository.findEmployeesByProject(projectId);
    }

    public Project getProjectDetails(int id) {

        Project p = repository.findProjectById(id);

        p.setSubProjects(repository.findSubprojects(id));
        p.setTasks(repository.findTasksByProject(id));
        p.setAssignedEmployees(getProjectEmployees(id));


        return p;
    }

    public List<Employee> getAvailableEmployeesForTask(int projectId, int taskId) {
        return repository.getAvailableEmployeesForTask(projectId,taskId);
    }

}


