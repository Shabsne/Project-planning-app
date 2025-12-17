package org.example.projectplanningapp.controllers;

import jakarta.servlet.http.HttpSession;
import org.example.projectplanningapp.models.Employee;
import org.example.projectplanningapp.models.Project;
import org.example.projectplanningapp.models.Status;
import org.example.projectplanningapp.models.Task;
import org.example.projectplanningapp.services.EmployeeService;
import org.example.projectplanningapp.services.ProjectService;
import org.example.projectplanningapp.services.TaskService;
import org.example.projectplanningapp.utils.SessionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
public class ProjectController {

    private final ProjectService projectService;
    private final EmployeeService employeeService;
    private final TaskService taskService;

    public ProjectController(ProjectService projectService, EmployeeService employeeService, TaskService taskService) {
        this.projectService = projectService;
        this.employeeService = employeeService;
        this.taskService = taskService;
    }


    @GetMapping("/projects")
    public String listProjects(Model model, HttpSession session) {
        Employee loggedIn = SessionUtils.requireLogin(session);

        // Hent kun projekter hvor brugeren er projektleder eller tildelt medarbejder
        List<Project> userProjects = projectService.getProjectsForEmployee(loggedIn.getEmployeeId());

        // Filtrer så kun parent projekter vises
        List<Project> parentProjects = userProjects.stream()
                .filter(p -> p.getParentProject() == null)
                .toList();

        model.addAttribute("projects", parentProjects);
        model.addAttribute("employee", loggedIn);
        return "project/list";
    }


    // Create project form
    @GetMapping("/projects/create")
    public String showCreateProjectForm(Model model, HttpSession session) {
        SessionUtils.requireLogin(session);

        model.addAttribute("project", new Project());
        model.addAttribute("leaders", employeeService.getAllEmployees());

        return "project/create";
    }


    @PostMapping("/projects/create")
    public String createProject(@ModelAttribute Project project, HttpSession session) {
        Employee loggedIn = SessionUtils.requireLogin(session);

        // Sæt projektleder hvis valgt
        if (project.getProjectLeaderId() != null) {
            Employee leader = employeeService.getEmployeeFromId(project.getProjectLeaderId());
            project.setProjectLeader(leader);
        }

        // Opret projektet først
        projectService.createProject(project);

        // Tildel automatisk den bruger der oprettede projektet
        List<Project> allProjects = projectService.getAllProjects();
        Project createdProject = allProjects.get(allProjects.size() - 1);

        // Tildel opretteren til projektet
        projectService.assignEmployeeToProject(createdProject.getId(), loggedIn.getEmployeeId());

        return "redirect:/projects/" + createdProject.getId();
    }



    @GetMapping("/projects/{parentId}/sub/create")
    public String showCreateSubProjectForm(@PathVariable int parentId, Model model, HttpSession session) {
        SessionUtils.requireLogin(session);

        Project subProject = new Project();
        Project parentProject = new Project();
        parentProject.setId(parentId);
        subProject.setParentProject(parentProject);

        model.addAttribute("project", subProject);
        model.addAttribute("leaders", employeeService.getAllEmployees());

        return "project/create-sub";
    }


    @PostMapping("/projects/{parentId}/sub/create")
    public String createSubProject(@PathVariable int parentId, @ModelAttribute Project project, HttpSession session) {
        Employee loggedIn = SessionUtils.requireLogin(session);

        // Hent projektleder hvis valgt
        if (project.getProjectLeaderId() != null) {
            Employee leader = employeeService.getEmployeeFromId(project.getProjectLeaderId());
            project.setProjectLeader(leader);
        }

        // Sæt parent project korrekt
        Project parentProject = new Project();
        parentProject.setId(parentId);
        project.setParentProject(parentProject);

        // Gem sub-projekt
        projectService.createProject(project);

        // Find det nyoprettede projekt og tildel opretteren
        List<Project> allProjects = projectService.getAllProjects();
        Project createdProject = allProjects.get(allProjects.size() - 1);
        projectService.assignEmployeeToProject(createdProject.getId(), loggedIn.getEmployeeId());

        return "redirect:/projects/" + createdProject.getId();
    }


    // Projekt detaljer
    @GetMapping("/projects/{id}")
    public String projectDetails(@PathVariable int id, Model model, HttpSession session) {
        Employee loggedIn = SessionUtils.requireLogin(session);

        Project project = projectService.getProjectDetails(id);

        // Hent friske data hver gang for at undgå cache-problemer
        List<Employee> assignedEmployees = projectService.getProjectEmployees(id);
        List<Employee> availableEmployees = projectService.getAvailableEmployeesForProject(id);
        int totalEstimatedHours = projectService.calculateEstimatedHours(project);

        // Opdater project med friske data
        project.setAssignedEmployees(assignedEmployees);

        model.addAttribute("project", project);
        model.addAttribute("status", Status.values());
        model.addAttribute("rootTasks", taskService.getRootTasks(id));
        model.addAttribute("totalEstimatedHours", totalEstimatedHours);
        model.addAttribute("availableEmployees", availableEmployees);

        return "project/details";
    }


    // Assign employee to project
    @PostMapping("/projects/{projectId}/assign-employee")
    public String assignEmployeeToProject(@PathVariable int projectId,
                                          @RequestParam int employeeId) {
        projectService.assignEmployeeToProject(projectId, employeeId);
        return "redirect:/projects/" + projectId;
    }

    // Remove employee from project
    @PostMapping("/projects/{projectId}/remove-employee/{employeeId}")
    public String removeEmployeeFromProject(@PathVariable int projectId,
                                            @PathVariable int employeeId) {
        projectService.removeEmployeeFromProject(projectId, employeeId);
        return "redirect:/projects/" + projectId;
    }


    // Edit project form
    @GetMapping("/projects/{id}/edit")
    public String editProjectForm(@PathVariable int id, Model model) {
        model.addAttribute("project", projectService.findById(id));
        model.addAttribute("leaders", employeeService.getAllEmployees());
        return "project/edit";
    }

    // Update project
    @PostMapping("/projects/{id}/edit")
    public String updateProject(@PathVariable int id, @ModelAttribute Project project) {
        project.setId(id);
        projectService.updateProject(project);

        return "redirect:/projects/" + id;
    }

    // Delete project
    @PostMapping("/projects/{id}/delete")
    public String deleteProject(@PathVariable int id) {
        projectService.deleteById(id);
        return "redirect:/projects";
    }

    @GetMapping("/status")
    public String showStatus(HttpSession session, Model model) {
        Employee loggedIn = SessionUtils.requireLogin(session);

        List<Project> assignedProjects = projectService.findProjectsByEmployee(loggedIn.getEmployeeId());

        Map<Integer, Integer> completionMap = new HashMap<>();

        // Tjekker om der findes en opgave i projektet
        Map<Integer, Boolean> hasTasksMap = new HashMap<>();


        for (Project project : assignedProjects) {
            // Færdiggørelse i procent
            int completionPercentage = projectService.calculateCompletionPercentage(project.getId());
            completionMap.put(project.getId(), completionPercentage);

            // 2. Tjekker om projektet har nogle opgaver
            boolean hasTasks = taskService.hasAnyTasksInProject(project.getId());
            hasTasksMap.put(project.getId(), hasTasks);
        }

        model.addAttribute("employee", loggedIn);
        model.addAttribute("projects", assignedProjects);
        model.addAttribute("completionMap", completionMap);
        model.addAttribute("hasTasksMap", hasTasksMap);

        return "status";
    }

    // Viser formularen til oprettelse af opgave
    @GetMapping("/projects/{projectId}/createTask")
    public String getCreateTaskView(@PathVariable int projectId, Model model, HttpSession session) {
        SessionUtils.requireLogin(session);

        Task newTask = new Task();
        newTask.setProjectId(projectId); // Sætter Project ID på Task objektet

        // Hent nødvendige data til formularen (Medarbejdere og Statusser)
        List<Employee> projectEmployees = projectService.getProjectEmployees(projectId);

        // Sætter statusserne som der bliver brugt i html.
        List<Status> statusOptions = List.of(Status.values());

        model.addAttribute("task", newTask);
        model.addAttribute("projectEmployees", projectEmployees);
        model.addAttribute("status", statusOptions);

        return "task/createTask";
    }


}