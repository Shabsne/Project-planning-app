package org.example.projectplanningapp.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.example.projectplanningapp.exceptions.UnauthorizedException;
import org.example.projectplanningapp.exceptions.ValidationException;
import org.example.projectplanningapp.models.*;
import org.example.projectplanningapp.services.ProjectService;
import org.example.projectplanningapp.services.TaskService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.example.projectplanningapp.services.EmployeeService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class EmployeeController {

    private final EmployeeService employeeService;
    private final TaskService taskService;
    private final ProjectService projectService;


    public EmployeeController(EmployeeService employeeService, TaskService taskService, ProjectService projectService) {
        this.taskService = taskService;
        this.employeeService = employeeService;
        this.projectService = projectService;
    }

    // Login side
    @GetMapping("/")
    public String showLogInPage() {
        System.out.println("Hej");
        return "logIn";
    }

    // Registrering af medarbejder
    @GetMapping("/employee/register")
    public String showRegisterEmployeeForm(Model model) {
        model.addAttribute("employee", new Employee());
        return "employee/register";
    }

    @PostMapping("/")
    public String loginUser(@RequestParam String email, @RequestParam String password,
                            Model model, HttpSession session) {

        try {
            //Service kaster ValidationException hvis login fejler
            Employee employee = employeeService.login(email, password);

            session.setAttribute("employee", employee);
            return "redirect:/employee/home/" + employee.getEmployeeId();

        } catch (ValidationException e) {
            //Vis fejl på login-siden
            model.addAttribute("error", true);
            model.addAttribute("errorMessage", e.getMessage());
            return "logIn";
        }
    }

    @GetMapping("/employee/home/{employeeId}")
    public String homePage(@PathVariable int employeeId, HttpSession session, Model model, HttpServletRequest request) {
        Employee loggedIn = (Employee) session.getAttribute("employee");

        if (loggedIn == null) {
            throw new UnauthorizedException("Du skal være logget ind for at se denne side");
        }

        if (loggedIn.getEmployeeId() != employeeId) {
            throw new UnauthorizedException("Du har ikke adgang til denne medarbejders side");
        }

        List<Task> nextTasks = taskService.getNextTasksForEmployee(employeeId);
        model.addAttribute("tasks", nextTasks);
        model.addAttribute("employee", loggedIn);
        model.addAttribute("referer", request.getHeader("referer")); // Tilføj referer-header til modelen

        return "homepage";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @PostMapping("/employee/register")
    public String registerEmployee(@ModelAttribute Employee employee,
                                   @RequestParam String confirmPassword, Model model) {
        try {
            if (!employee.getPassword().equals(confirmPassword)) {
                throw new ValidationException("Adgangskoder matcher ikke");
            }

            employee.setRole(Role.DEVELOPER);
            employeeService.registerEmployee(employee); //Service validerer resten

            return "redirect:/employee/list";

        } catch (ValidationException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("employee", employee);
            model.addAttribute("confirmPassword", confirmPassword);
            return "employee/register";
        }
    }

    // Rediger profil
    @GetMapping("/employee/{id}/edit")
    public String showEditProfileForm(@PathVariable int id, Model model, HttpSession session) {
        Employee loggedIn = (Employee) session.getAttribute("employee");

        if (loggedIn == null) {
            return "redirect:/";
        }

        //Tjek om Employee må redigere denne profil
        if (!loggedIn.isAdmin() && loggedIn.getEmployeeId() != id) {
            throw new UnauthorizedException("Du har ikke tilladelse til at redigere denne profil");
        }

        Employee target = employeeService.getEmployeeFromId(id);

        model.addAttribute("employee", target);
        model.addAttribute("canEditRole", loggedIn.isAdmin());

        return "employee/edit";
    }

    @PostMapping("/employee/{id}/edit")
    public String updateProfile(@PathVariable int id, @ModelAttribute Employee employee,
                                @RequestParam(required = false) Integer roleId, HttpSession session) {

        Employee loggedIn = (Employee) session.getAttribute("employee");

        //Employee må kun ændre sin egen profil
        if (!loggedIn.isAdmin() && loggedIn.getEmployeeId() != id) {
            return "redirect:/employee/home/" + loggedIn.getEmployeeId();
        }

        //Admin ændrer rolle
        if (loggedIn.isAdmin() && roleId != null) {
            employeeService.updateEmployeeRole(id, roleId);
        }

        employee.setEmployeeId(id);
        employeeService.updateOwnProfile(employee);

        return "redirect:/employee/" + id;
    }

    // Liste af medarbejdere
    @GetMapping("/employee/list")
    public String listEmployees(Model model, HttpSession session) {
        Employee loggedIn = (Employee) session.getAttribute("employee");

        if (loggedIn == null) return "redirect:/";
        model.addAttribute("employees", employeeService.getAllEmployees());
        return "employee/list";
    }

    @GetMapping("/employee/{id}/myTasks")
    public String myTasks(@PathVariable int id, HttpSession session, Model model) {
        Employee loggedIn = (Employee) session.getAttribute("employee");

        if (loggedIn == null) {
            return "redirect:/";
        }

        //SPØRG NICOLAI OM NØDVENDIGT

        if (loggedIn.getEmployeeId() != id) {
            return "redirect:/employee/" + loggedIn.getEmployeeId() + "/myTasks";
        }

        List<Task> tasks = taskService.getAssignedTasksForEmployee(id);
        model.addAttribute("tasks", tasks);

        model.addAttribute("employee", loggedIn);

        return "task/myTasks"; // mapper til templates/task/myTasks.html
    }

    @GetMapping("/employee/{id}")
    public String viewEmployeeMenu(@PathVariable int id, Model model, HttpSession session) {
        Employee loggedIn = (Employee) session.getAttribute("employee");

        if (loggedIn == null) return "redirect:/";

        Employee employee = employeeService.getEmployeeFromId(id);
        if (employee == null) return "redirect:/employee/home/" + loggedIn.getEmployeeId();

        model.addAttribute("employee", employee);
        return "employee/employeeInformation";
    }

    @PostMapping("employee/{id}/delete")
    public String deleteEmployee(@PathVariable int id) {
        employeeService.deleteEmployee(id);
        return "redirect:/employee/list";
    }

    @PostMapping("employee/{id}/role")
    public String changeRole(@PathVariable int id, @RequestParam String role) {
        Role newRole;
        if ("admin".equalsIgnoreCase(role)) {
            newRole = Role.ADMIN;
        } else {
            newRole = Role.DEVELOPER;
        }
        employeeService.changeRole(id, newRole);
        return "redirect:/employee/list";
    }

    //Status
    @GetMapping("/status")
    public String showStatus(HttpSession session, Model model) {
        Employee loggedIn = (Employee) session.getAttribute("employee");

        if (loggedIn == null) {
            return "redirect:/";
        }

        List<Project> assignedProjects = projectService.findProjectsByEmployee(loggedIn.getEmployeeId());

        Map<Integer, Integer> completionMap = new HashMap<>();
        for (Project project : assignedProjects) {
            int completionPercentage = projectService.calculateCompletionPercentage(project.getId());
            completionMap.put(project.getId(), completionPercentage);
        }

        model.addAttribute("employee", loggedIn);
        model.addAttribute("projects", assignedProjects);
        model.addAttribute("completionMap", completionMap);

        return "status";
    }
}
