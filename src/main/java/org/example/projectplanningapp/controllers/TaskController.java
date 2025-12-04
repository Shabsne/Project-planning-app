package org.example.projectplanningapp.controllers;

import org.example.projectplanningapp.models.Employee;
import org.example.projectplanningapp.models.Project;
import org.example.projectplanningapp.models.Status;
import org.example.projectplanningapp.models.Task;
import org.example.projectplanningapp.services.EmployeeService;
import org.example.projectplanningapp.services.ProjectService;
import org.example.projectplanningapp.services.TaskService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/projects/{projectId}/tasks")
public class TaskController {

    private final TaskService taskService;
    private final ProjectService projectService;
    private final EmployeeService employeeService;

    public TaskController(TaskService taskService, ProjectService projectService, EmployeeService employeeService) {
        this.taskService = taskService;
        this.projectService = projectService;
        this.employeeService = employeeService;
    }

    // Vis create-task formular
    @GetMapping("/create")
    public String showCreateForm(@PathVariable int projectId, Model model) {
        Task task = new Task();
        task.setProjectId(projectId); // <-- sæt projectId her
        model.addAttribute("task", task);
        model.addAttribute("projectEmployees", projectService.getProjectEmployees(projectId));
        model.addAttribute("status", Status.values());
        return "task/createTask";
    }


    // Opret task
    @PostMapping("/create")
    public String createTask(@PathVariable int projectId, @ModelAttribute Task task) {
        // Sæt projektId
        task.setProjectId(projectId);

        // Konverter employee IDs til Employee objekter
        if (task.getAssignedEmployeeIds() != null && !task.getAssignedEmployeeIds().isEmpty()) {
            List<Employee> assigned = task.getAssignedEmployeeIds().stream()
                    .map(employeeService::getEmployeeFromId)
                    .collect(Collectors.toList());
            task.setAssignedEmployees(assigned);
        }

        // Gem task
        taskService.createTask(task);
        return "redirect:/projects/" + projectId;
    }

    // Vis task detaljer
    @GetMapping("/{taskId}")
    public String showTaskDetails(@PathVariable int projectId,
                                  @PathVariable int taskId,
                                  Model model) {

        Task task = taskService.getTaskFromId(taskId);
        Project project = projectService.getProjectDetails(projectId);

        model.addAttribute("task", task);
        model.addAttribute("project", project);
        model.addAttribute("status", Status.values());

        List<Employee> assigned = taskService.getAssignedEmployeesForTask(taskId);
        List<Employee> available = projectService.getAvailableEmployeesForTask(projectId, taskId);

        model.addAttribute("assignedEmployees", assigned);
        model.addAttribute("availableEmployees", available);

        return "task/taskDetails";
    }



    @PostMapping("/{taskId}/assign-employee/{employeeId}")
    public String assignEmployeeToTask(@PathVariable int projectId,
                                 @PathVariable int taskId,
                                 @PathVariable int employeeId) {

        taskService.assignEmployeeToTask(taskId, employeeId);

        return "redirect:/projects/" + projectId + "/tasks/" + taskId;
    }

    @PostMapping("/{taskId}/remove-employee/{employeeId}")
    public String removeEmployee(@PathVariable int projectId,
                                 @PathVariable int taskId,
                                 @PathVariable int employeeId) {

        taskService.removeEmployeeFromTask(taskId, employeeId);

        return "redirect:/projects/" + projectId + "/tasks/" + taskId;
    }



    @PostMapping("{taskId}/update")
    public String updateTask(
            @PathVariable int projectId,
            @PathVariable int taskId,
            @ModelAttribute Task task
    ) {
        // Mapper employeeIds → Employee objects
        if (task.getAssignedEmployeeIds() != null) {
            List<Employee> employees = task.getAssignedEmployeeIds().stream()
                    .map(employeeService::getEmployeeFromId)
                    .toList();
            task.setAssignedEmployees(employees);
        }

        task.setTaskId(taskId);
        task.setProjectId(projectId);

        taskService.updateTask(task);

        return "redirect:/projects/" +projectId+"/tasks/" +taskId;
    }

}
