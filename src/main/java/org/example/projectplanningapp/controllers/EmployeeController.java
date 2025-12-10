package org.example.projectplanningapp.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.example.projectplanningapp.models.Employee;
import org.example.projectplanningapp.models.Task;
import org.example.projectplanningapp.services.TaskService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.example.projectplanningapp.services.EmployeeService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class EmployeeController {

    private final EmployeeService employeeService;
    private final TaskService taskService;


    public EmployeeController(EmployeeService employeeService, TaskService taskService) {
        this.taskService = taskService;
        this.employeeService = employeeService;
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
        Employee employee = employeeService.login(email, password);

        if (employee == null) {
            model.addAttribute("error", true);
            return "logIn";
        }

        session.setAttribute("employee", employee);
        return "redirect:/employee/home/" + employee.getEmployeeId();
    }

    @GetMapping("/employee/home/{employeeId}")
    public String homePage(@PathVariable int employeeId, HttpSession session, Model model, HttpServletRequest request) {
        Employee loggedIn = (Employee) session.getAttribute("employee");

        if (loggedIn == null) return "redirect:/";
        if (loggedIn.getEmployeeId() != employeeId)
            return "redirect:/employee/home/" + loggedIn.getEmployeeId();

        List<Task> nextTasks = taskService.getNextTasksForEmployee(employeeId);
        model.addAttribute("tasks", nextTasks);
        model.addAttribute("employee", loggedIn);

        // Tilføj referer-header til modelen
        model.addAttribute("referer", request.getHeader("referer"));

        return "homepage";
    }





    @GetMapping("/employee/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @PostMapping("/employees/register")
    public String registerEmployee(@ModelAttribute Employee employee,
                                   @RequestParam String confirmPassword, Model model) {
        if (employeeService.emailExists(employee.getEmail())) {
            model.addAttribute("error", "Emailen findes allerede");
            return "employee/register";
        }

        if (!employee.getPassword().equals(confirmPassword)) {
            model.addAttribute("error", "Adgangskoder matcher ikke!");
            return "employee/register";
        }

        employeeService.registerEmployee(employee);
        return "redirect:/";
    }

    // Rediger profil
    @GetMapping("/employee/{id}/edit")
    public String showEditProfileForm(@PathVariable int id, Model model, HttpSession session) {
        Employee loggedIn = (Employee) session.getAttribute("employee");

        if (loggedIn == null) return "redirect:/";

        //Employee kan kun redigere sig selv
        if (!loggedIn.isAdmin() && loggedIn.getEmployeeId() != id) {
            return "redirect:/employee/home/" + loggedIn.getEmployeeId();
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

        return "redirect:/employee/" + id + "/edit";
    }

    // Liste af medarbejdere
    @GetMapping("/employee/list")
    public String listEmployees(Model model) {
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




}
