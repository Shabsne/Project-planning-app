package org.example.projectplanningapp.controllers;

import jakarta.servlet.http.HttpSession;
import org.example.projectplanningapp.models.Employee;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.example.projectplanningapp.services.EmployeeService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // Login side
    @GetMapping("/")
    public String showLogInPage() {
        System.out.println("Hej");
        return "logIn";
    }

    // Registrering af medarbejder
    @GetMapping("/employees/register")
    public String showRegisterEmployeeForm(Model model) {
        model.addAttribute("employee", new Employee());
        return "employees/register";
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
        return "redirect:/employees/home/" + employee.getEmployeeId();
    }

    @GetMapping("/employees/home/{employeeId}")
    public String homePage(@PathVariable int employeeId, HttpSession session) {
        Employee loggedIn = (Employee) session.getAttribute("employee");

        if (loggedIn == null) return "redirect:/employees/login";
        if (loggedIn.getEmployeeId() != employeeId)
            return "redirect:/employees/home/" + loggedIn.getEmployeeId();

        return "homepage";
    }

    @GetMapping("/employees/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:";
    }

    @PostMapping("/employees/register")
    public String registerEmployee(@ModelAttribute Employee employee,
                                   @RequestParam String confirmPassword, Model model) {
        if (employeeService.emailExists(employee.getEmail())) {
            model.addAttribute("error", "Emailen findes allerede");
            return "employees/register";
        }

        if (!employee.getPassword().equals(confirmPassword)) {
            model.addAttribute("error", "Adgangskoder matcher ikke!");
            return "employees/register";
        }

        employeeService.registerEmployee(employee);
        return "redirect:/employees/login";
    }

    // Rediger profil
    @GetMapping("/employees/{id}/edit")
    public String showEditProfileForm(@PathVariable int id, Model model) {
        model.addAttribute("employee", employeeService.getEmployeeFromId(id));
        return "employee/edit";
    }

    @PostMapping("/employees/{id}/edit")
    public String updateProfile(@PathVariable int id, @ModelAttribute Employee employee) {
        employee.setEmployeeId(id);
        employeeService.updateOwnProfile(employee);
        return "redirect:/employees/" + id + "/edit";
    }

    // Liste af medarbejdere
    @GetMapping("/employees/list")
    public String listEmployees(Model model) {
        model.addAttribute("employees", employeeService.getAllEmployees());
        return "employees/list";
    }

}
