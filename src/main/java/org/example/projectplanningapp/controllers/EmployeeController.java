package org.example.projectplanningapp.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.example.projectplanningapp.exceptions.UnauthorizedException;
import org.example.projectplanningapp.exceptions.ValidationException;
import org.example.projectplanningapp.models.*;
import org.example.projectplanningapp.services.ProjectService;
import org.example.projectplanningapp.services.TaskService;
import org.example.projectplanningapp.utils.SessionUtils;
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
        Employee loggedIn = SessionUtils.requireLogin(session);

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
            //Vis fejl på registreringssiden
            model.addAttribute("error", e.getMessage());
            model.addAttribute("employee", employee);
            model.addAttribute("confirmPassword", confirmPassword);
            return "employee/register";
        }
    }

    // Rediger profil
    @GetMapping("/employee/{id}/edit")
    public String showEditProfileForm(@PathVariable int id, Model model, HttpSession session) {
        Employee loggedIn = SessionUtils.requireLogin(session);
        SessionUtils.requireSelfOrAdmin(loggedIn, id);

        //getEmployeeFromId kaster ResourceNotFoundException hvis ikke fundet
        Employee target = employeeService.getEmployeeFromId(id);

        model.addAttribute("employee", target);
        model.addAttribute("canEditRole", loggedIn.isAdmin());

        return "employee/edit";
    }

    @PostMapping("/employee/{id}/edit")
    public String updateProfile(@PathVariable int id, @ModelAttribute Employee employee, HttpSession session) {

        Employee loggedIn = SessionUtils.requireLogin(session);
        SessionUtils.requireSelfOrAdmin(loggedIn, id);

        employee.setEmployeeId(id);
        employeeService.updateOwnProfile(employee); //Validerer automatisk at employee eksiterer

        return "redirect:/employee/" + id;
    }

    @PostMapping("employee/{id}/role")
    public String changeRole(@PathVariable int id, @RequestParam String role, HttpSession session) {

        Employee loggedIn = SessionUtils.requireLogin(session);
        SessionUtils.requireAdmin(loggedIn);

        //Valider at employee eksisterer
        employeeService.getEmployeeFromId(id);

        Role newRole;
        if ("admin".equalsIgnoreCase(role)) {
            newRole = Role.ADMIN;
        } else {
            newRole = Role.DEVELOPER;
        }

        employeeService.changeRole(id, newRole);
        return "redirect:/employee/list";
    }

    // Liste af medarbejdere
    @GetMapping("/employee/list")
    public String listEmployees(Model model, HttpSession session) {
        Employee loggedIn = SessionUtils.requireLogin(session);

        model.addAttribute("employees", employeeService.getAllEmployees());
        return "employee/list";
    }

    @GetMapping("/employee/{id}/myTasks")
    public String myTasks(@PathVariable int id, HttpSession session, Model model) {
        Employee loggedIn = SessionUtils.requireLogin(session);

        if (loggedIn.getEmployeeId() != id) {
            throw new UnauthorizedException("Du kan kun se dine egne opgaver");
        }

        List<Task> tasks = taskService.getAssignedTasksForEmployee(id);
        model.addAttribute("tasks", tasks);
        model.addAttribute("employee", loggedIn);

        return "task/myTasks";
    }

    @GetMapping("/employee/{id}")
    public String viewEmployeeMenu(@PathVariable int id, Model model, HttpSession session) {
        Employee loggedIn = SessionUtils.requireLogin(session);

        //Employee kaster ResourceNotFoundException hvis ikke fundet
        Employee employee = employeeService.getEmployeeFromId(id);

        model.addAttribute("employee", employee);
        return "employee/employeeInformation";
    }

    @PostMapping("employee/{id}/delete")
    public String deleteEmployee(@PathVariable int id, HttpSession session) {
        Employee loggedIn = SessionUtils.requireLogin(session);
        SessionUtils.requireAdmin(loggedIn);

        //Valider at employee eksisterer før sletning
        employeeService.getEmployeeFromId(id);
        employeeService.deleteEmployee(id);

        return "redirect:/employee/list";
    }

}
