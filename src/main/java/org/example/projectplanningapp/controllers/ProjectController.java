package org.example.projectplanningapp.controllers;

import org.example.projectplanningapp.models.Employee;
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


    // List all projects
    @GetMapping("/projects")
    public String listProjects(Model model) {
        model.addAttribute("projects", projectService.getAllProjects());
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

        Project sub = new Project();
        Project parentRef = new Project();
        parentRef.setId(parentId);
        sub.setParentProject(parentRef);

        model.addAttribute("project", sub);
        model.addAttribute("leaders", employeeService.getAllEmployees());
        return "project/create-sub";
    }

    @PostMapping("/projects/sub/create")
    public String createSubProject(@ModelAttribute Project subproject) {
        projectService.createProject(subproject);
        return "redirect:/projects/" + subproject.getParentProject().getId();
    }

    // Details
    @GetMapping("/projects/{id}")
    public String projectDetails(@PathVariable int id, Model model) {
        model.addAttribute("project", projectService.getProjectDetails(id));
        return "project/details";
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

        return "redirect:/projects";
    }

    // Delete
    @PostMapping("/projects/{id}/delete")
    public String deleteProject(@PathVariable int id) {
        projectService.deleteById(id);
        return "redirect:/projects";
    }
}
