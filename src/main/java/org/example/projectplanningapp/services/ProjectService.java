package org.example.projectplanningapp.services;

import org.example.projectplanningapp.models.Employee;
import org.example.projectplanningapp.models.Project;
import org.example.projectplanningapp.repositories.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public void createProject(Project project) {
        projectRepository.createProject(project);
    }

    public List<Project> getAllProjects() {
        return projectRepository.getAllProjects();
    }

    public void deleteById(int id) {
        projectRepository.deleteProjectById(id);
    }

    public Project findById(int id) {
        return projectRepository.findProjectById(id);
    }

    public void updateProject(Project project) {
        projectRepository.update(project);
    }

    public List<Employee> getProjectEmployees(int projectId){
        return projectRepository.findEmployeesByProject(projectId);
    }

    public Project getProjectDetails(int id) {

        Project p = projectRepository.findProjectById(id);

        p.setSubProjects(projectRepository.findSubprojects(id));
        p.setTasks(projectRepository.findTasksByProject(id));
        p.setAssignedEmployees(getProjectEmployees(id));


        return p;
    }

    public List<Employee> getAvailableEmployeesForTask(int projectId, int taskId) {
        return projectRepository.getAvailableEmployeesForTask(projectId,taskId);
    }

}


