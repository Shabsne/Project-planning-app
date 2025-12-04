package org.example.projectplanningapp.services;

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

    public Project getProjectDetails(int id) {

        Project p = repository.findProjectById(id);

        p.setSubProjects(repository.findSubprojects(id));
        p.setTasks(repository.findTasksByProject(id));
        p.setAssignedEmployees(repository.findEmployeesByProject(id));


        return p;
    }


}


