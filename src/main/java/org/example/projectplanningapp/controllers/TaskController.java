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
        Project project = projectService.findById(projectId);

        List<Employee> assigned = taskService.getAssignedEmployeesForTask(taskId);
        List<Employee> available = projectService.getAvailableEmployeesForTask(projectId, taskId);

        // Hent subtasks hvis dette er en parent-task
        List<Task> subtasks = taskService.getSubTasks(taskId);

        model.addAttribute("task", task);
        model.addAttribute("project", project);
        model.addAttribute("assignedEmployees", assigned);
        model.addAttribute("availableEmployees", available);
        model.addAttribute("status", Status.values());
        model.addAttribute("subtasks", subtasks); // <-- MANGLEDE

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


    @PostMapping("/{taskId}/update-status")
    public String updateTaskStatus(@PathVariable int projectId,
                                   @PathVariable int taskId,
                                   @RequestParam("status") Status status) {

        Task task = taskService.getTaskFromId(taskId);
        task.setStatus(status);
        taskService.updateTask(task); // Opdater i databasen

        return "redirect:/projects/" + projectId;
    }

    @PostMapping("/{taskId}/updateFromProjectView")
    public String updateTask(@PathVariable int projectId,
                             @PathVariable int taskId,
                             @RequestParam String status) {
        Task task = taskService.getTaskFromId(taskId);
        task.setStatus(Status.valueOf(status));
        taskService.updateTask(task);
        return "redirect:/projects/" + projectId;
    }

    @GetMapping("/{taskId}/createSubTask")
    public String showCreateSubTaskForm(@PathVariable int projectId,
                                        @PathVariable int taskId,
                                        Model model) {

        Task parent = taskService.getTaskFromId(taskId);

        Task newSubTask = new Task();
        newSubTask.setProjectId(projectId);
        newSubTask.setParentTaskId(taskId);

        List<Employee> projectEmployees = projectService.getProjectEmployees(projectId);

        model.addAttribute("project", projectService.getProjectDetails(projectId));
        model.addAttribute("parentTask", parent);
        model.addAttribute("task", newSubTask); // ← matcher HTML th:object="${task}"
        model.addAttribute("availableEmployees", projectEmployees);
        model.addAttribute("status", Status.values());

        return "task/createSubTask";
    }

    @PostMapping("/{parentTaskId}/createSubTask")
    public String createSubTask(@PathVariable int projectId,
                                @PathVariable int parentTaskId,
                                @ModelAttribute("task") Task task) {

        // Sæt parent-felter
        task.setProjectId(projectId);
        task.setParentTaskId(parentTaskId);

        // 1. Opret subtask (skaber taskId)
        taskService.createTask(task);

        // 2. Assign employees
        if (task.getAssignedEmployeeIds() != null) {
            for (Integer empId : task.getAssignedEmployeeIds()) {
                taskService.assignEmployeeToTask(task.getTaskId(), empId);
            }
        }

        return "redirect:/projects/" + projectId + "/tasks/" + parentTaskId;
    }


    @PostMapping("/{taskId}/delete")
    public String deleteTask(@PathVariable int projectId, @PathVariable int taskId) {
        taskService.deleteTask(taskId);
        return "redirect:/projects/" + projectId;
    }

    @PostMapping("/{taskId}/update-status-ajax")
    @ResponseBody
    public String updateTaskStatusAjax(@PathVariable int projectId,
                                       @PathVariable int taskId,
                                       @RequestParam("status") Status status) {

        Task task = taskService.getTaskFromId(taskId);
        if(task != null) {
            task.setStatus(status);
            taskService.updateTask(task);
            return "OK"; // Return OK til formen
        }
        return "ERROR";
    }
















}
