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

@RequestMapping("/employees")
@Controller
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("")
    public String showLogInPage() {
        System.out.println("Hej");
        return "logIn";
    }

    //Registrering af medarbejder
    @GetMapping("/register")
    public String showRegisterEmployeeForm(Model model) {
        model.addAttribute("employee", new Employee());
        return "employee/register";
        return "register";
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam String email, @RequestParam String password, Model model, HttpSession session) {

        Employee employee = employeeService.login(email, password);

        if (employee == null) {
            model.addAttribute("error", true);
            return "logIn";
        }

        session.setAttribute("employee", employee);

        return "redirect:/home/" + employee.getEmployeeId();
    }

    @GetMapping("/home/{employeeId}")
    public String homePage(@PathVariable int employeeId, HttpSession session) {

        Employee loggedIn = (Employee) session.getAttribute("employee");

        if (loggedIn == null) {
            return "redirect:/";
        }

        if (loggedIn.getEmployeeId() != employeeId) {
            return "redirect:/home/" + loggedIn.getEmployeeId();
        }

        return "homepage";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();

        return "redirect:/";
    }

    @PostMapping("/register")
    public String registerEmployee(@ModelAttribute Employee employee,
                                   @RequestParam String confirmPassword, Model model) {
        //Tjek om email allerede findes
        if (employeeService.emailExists(employee.getEmail())) {
            model.addAttribute("error", "Emailen findes allerede");
            return "employee/register";
        }

        //Tjek om adgangskode matcher
        if (!employee.getPassword().equals(confirmPassword)) {
            model.addAttribute("error", "Adgangskoder matcher ikke!");
            return "employee/register";
        }

        //Opret brugeren i databasen
        employeeService.registerEmployee(employee);
        return "redirect:/logIn"; //redirect til login-side efter oprettelse
    }

    //Rediger profil
    @GetMapping("/{id}/edit")
    public String showEditProfileForm(@PathVariable int id, Model model) {
        model.addAttribute("employee", employeeService.getEmployeeFromId(id));
        return "employee/edit";
    }

    @PostMapping("/{id}/edit")
    public String updateProfile(@PathVariable int id, @ModelAttribute Employee employee) {
        employee.setEmployeeId(id);
        employeeService.updateOwnProfile(employee);

        return "redirect:/employee/" + id + "/edit"; //Redirect til egen profil efter gemt ændringer
    }

    //Liste af medarbejdere
    @GetMapping
    public String listEmployees(Model model) {
        model.addAttribute("employees", employeeService.getAllEmployees());
        return "employee/list";
    }

}
