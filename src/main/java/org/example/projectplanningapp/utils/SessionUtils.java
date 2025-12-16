package org.example.projectplanningapp.utils;

import jakarta.servlet.http.HttpSession;
import org.example.projectplanningapp.exceptions.UnauthorizedException;
import org.example.projectplanningapp.models.Employee;
import org.hibernate.Session;

public final class SessionUtils {

    //Forhindrer instansiering
    private SessionUtils() {}


    //Sikrer at brugeren er logget ind, kaster UnauthorizedException hvis ikke.
    public static Employee requireLogin(HttpSession session) {
        Employee employee = (Employee) session.getAttribute("employee");
        if (employee == null) {
            throw new UnauthorizedException("Du skal være logget ind");
        }
        return employee;
    }

    //Sikrer at brugeren er administrator
    public static void requireAdmin(Employee employee) {
        if (employee == null || !employee.isAdmin()) {
            throw new UnauthorizedException("Kun administratorer har adgang til denne funktion");
        }
    }

    //Sikrer at brugeren kun tilgår sine egne data (medmindre admin)
    public static void requireSelfOrAdmin(Employee loggedIn, int targetEmployeeId) {
        if (!loggedIn.isAdmin() && loggedIn.getEmployeeId() != targetEmployeeId) {
            throw new UnauthorizedException("Du har ikke adgang til denne medarbejders data");
        }
    }
}
