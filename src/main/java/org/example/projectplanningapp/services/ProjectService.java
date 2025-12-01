package org.example.projectplanningapp.services;

import org.example.projectplanningapp.models.Project;
import org.example.projectplanningapp.repositories.ProjectRepository;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {

    private ProjectRepository repository;

    public ProjectService(ProjectRepository repository) {
        this.repository = repository;
    }


    public void createProject(Project project) {
        repository.createProject(project);
    }

}


