package org.example.projectplanningapp.controllers;

import org.example.projectplanningapp.models.Project;
import org.example.projectplanningapp.models.Task;
import org.example.projectplanningapp.services.ProjectService;
import org.example.projectplanningapp.services.TaskService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
public class TaskController {

    private final TaskService taskService;
    private final ProjectService projectService;

    public TaskController(TaskService taskService, ProjectService projectService) {
        this.taskService = taskService;
        this.projectService = projectService;
    }

    // Viser formularen
    @GetMapping("/projects/{projectId}/createTask")
    public String showCreateTaskForm(@PathVariable int projectId, Model model) {
        Task task = new Task();
        Project project = projectService.findById(projectId);
        task.setParentProject(project);
        model.addAttribute("task",task);
        return "createTaskForm";
    }

    // Modtager form-data
    @PostMapping("/createTask")
    public String createTask(@ModelAttribute Task task) {
        taskService.createTask(task);
        return "redirect:/projects/" + task.getParentProject().getId();
    }

}