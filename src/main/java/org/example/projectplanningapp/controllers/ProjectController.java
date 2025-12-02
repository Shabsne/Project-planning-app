package org.example.projectplanningapp.controllers;

import org.example.projectplanningapp.models.Project;
import org.example.projectplanningapp.services.EmployeeService;
import org.example.projectplanningapp.services.ProjectService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ProjectController {

    private final ProjectService projectService;
    private final EmployeeService employeeService;

    public ProjectController(ProjectService projectService, EmployeeService employeeService) {
        this.projectService = projectService;
        this.employeeService = employeeService;
    }

    // Viser Opret Projekt siden
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("project", new Project());
        model.addAttribute("leaders", employeeService.getAllEmployees());
        return "project/create";
    }

    // POST: Gemmer projektet
    @PostMapping("/create")
    public String createProject(@ModelAttribute Project project) {

        projectService.createProject(project);
        return "redirect:/projects";
    }

    // ---------- SUBPROJECT ---------------------

    @GetMapping("/{parentId}/create-sub")
    public String showCreateSubprojectForm(@PathVariable int parentId, Model model) {

        Project sub = new Project();
        Project parentRef = new Project();
        parentRef.setId(parentId);

        sub.setParentProject(parentRef);

        model.addAttribute("project", sub);
        model.addAttribute("leaders", employeeService.getAllEmployees());
        return "project/create-sub";
    }

    @PostMapping("/create-sub")
    public String createSubproject(@ModelAttribute Project subproject) {

        projectService.createProject(subproject);
        return "redirect:/projects/" + subproject.getParentProject().getId();
    }
}
