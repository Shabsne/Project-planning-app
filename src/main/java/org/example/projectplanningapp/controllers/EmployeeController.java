package org.example.projectplanningapp.controllers;

import jakarta.servlet.http.HttpSession;
import org.example.projectplanningapp.models.Employee;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping("")
    public String showLogInPage() {
        System.out.println("Hej");
        return "logIn";
    }

    @GetMapping("/register")
    public String showRegisterEmployeeForm(Model model) {
        model.addAttribute("employee", new Employee());
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
            return "registerEmployee";
        }

        //Tjek om adgangskode matcher
        if (!employee.getPassword().equals(confirmPassword)) {
            model.addAttribute("error", "Adgangskoder matcher ikke!");
            return "registerEmployee";
        }

        //Opret brugeren i databasen
        employeeService.registerEmployee(employee);
    return "redirect:/logIn"; //redirect til login-side efter oprettelse
    }
}
