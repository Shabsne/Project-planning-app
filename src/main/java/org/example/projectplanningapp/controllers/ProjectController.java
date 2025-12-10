package org.example.projectplanningapp.controllers;

import org.example.projectplanningapp.models.Employee;
import org.example.projectplanningapp.models.Project;
import org.example.projectplanningapp.models.Status;
import org.example.projectplanningapp.services.EmployeeService;
import org.example.projectplanningapp.services.ProjectService;
import org.example.projectplanningapp.services.TaskService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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


    // list projects - Kun parents projekter
    @GetMapping("/projects")
    public String listProjects(Model model) {
        List<Project> allProjects = projectService.getAllProjects();

        // Filtrer så kun parent projekter vises
        List<Project> parentProjects = allProjects.stream()
                .filter(p -> p.getParentProject() == null)
                .toList();

        model.addAttribute("projects", parentProjects);
        return "project/list";
    }


    // Create project
    @GetMapping("/projects/create")
    public String showCreateForm(Model model) {
        model.addAttribute("project", new Project());
        model.addAttribute("leaders", employeeService.getAllEmployees());
        return "project/create";
    }

    @PostMapping("/projects/create")
    public String createProject(@ModelAttribute Project project) {
        if (project.getProjectLeaderId() != null) {
            Employee leader = employeeService.getEmployeeFromId(project.getProjectLeaderId());
            project.setProjectLeader(leader);
        }
        projectService.createProject(project);
        return "redirect:/projects";
    }


    // Create Sub-project
    @GetMapping("/projects/{parentId}/sub/create")
    public String showCreateSubForm(@PathVariable int parentId, Model model) {
        // Opret nyt sub-projekt
        Project subProject = new Project();

        // Sæt parent project reference
        Project parentProject = new Project();
        parentProject.setId(parentId);
        subProject.setParentProject(parentProject);

        // Tilføj til model
        model.addAttribute("project", subProject);
        model.addAttribute("leaders", employeeService.getAllEmployees());

        return "project/create-sub";
    }

    @PostMapping("/projects/{parentId}/sub/create")
    public String createSubProject(@PathVariable int parentId, @ModelAttribute Project project) {
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

        return "redirect:/projects";
    }


    // Details
    @GetMapping("/projects/{id}")
    public String projectDetails(@PathVariable int id, Model model) {
        Project project = projectService.getProjectDetails(id);

        int totalEstimatedHours = projectService.calculateEstimatedHours(project);

        model.addAttribute("project", project);
        model.addAttribute("status", Status.values());
        model.addAttribute("rootTasks", taskService.getRootTasks(id));
        model.addAttribute("totalEstimatedHours", totalEstimatedHours);

        return "project/details";
    }

    @PostMapping("/projects/{projectId}/assign-employee")
    public String assignEmployeeToProject(@PathVariable int projectId,
                                          @RequestParam int employeeId,
                                          Model model) {
        projectService.assignEmployeeToProject(projectId, employeeId);
        return "redirect:/projects/" + projectId;
    }

    @PostMapping("/projects/{projectId}/remove-employee/{employeeId}")
    public String removeEmployeeFromProject(@PathVariable int projectId,
                                            @PathVariable int employeeId) {
        projectService.removeEmployeeFromProject(projectId, employeeId);
        return "redirect:/projects/" + projectId;
    }


    // Edit
    @GetMapping("/projects/{id}/edit")
    public String editProjectForm(@PathVariable int id, Model model) {
        model.addAttribute("project", projectService.findById(id));
        model.addAttribute("leaders", employeeService.getAllEmployees());
        return "project/edit";
    }

    @PostMapping("/projects/{id}/edit")
    public String updateProject(@PathVariable int id, @ModelAttribute Project project) {
        project.setId(id);
        projectService.updateProject(project);

        return "redirect:/projects/" + id;
    }

    // Delete
    @PostMapping("/projects/{id}/delete")
    public String deleteProject(@PathVariable int id) {
        projectService.deleteById(id);
        return "redirect:/projects";
    }
}
