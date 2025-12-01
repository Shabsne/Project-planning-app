package org.example.projectplanningapp.services;

import org.example.projectplanningapp.models.Task;
import org.example.projectplanningapp.repositories.TaskRepository;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public int createTask(Task task) {
        return taskRepository.createTask(task);
    }
}
