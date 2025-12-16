package org.example.projectplanningapp.utils;

import jakarta.servlet.http.HttpSession;
import org.example.projectplanningapp.exceptions.UnauthorizedException;
import org.example.projectplanningapp.models.Employee;

public class SessionUtils {

    public static Employee requireLogin(HttpSession session) {
        Employee employee = (Employee) session.getAttribute("employee");
        if (employee == null) {
            throw new UnauthorizedException("Du skal være logget ind");
        }
        return employee;
    }
}
