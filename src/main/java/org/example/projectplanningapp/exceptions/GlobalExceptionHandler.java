package org.example.projectplanningapp.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public String handleResourceNotFound(ResourceNotFoundException ex, Model model, HttpServletRequest request) {
        model.addAttribute("errorMessage", ex.getMessage());
        model.addAttribute("errorCode", "404");
        model.addAttribute("requestUrl", request.getRequestURI());

        return "error/404";
    }

    @ExceptionHandler(UnauthorizedException.class)
    public String handleUnauthorized(UnauthorizedException ex, Model model, HttpServletRequest request) {
        model.addAttribute("errorMessage", ex.getMessage());
        model.addAttribute("errorCode", "403");
        model.addAttribute("requestUrl", request.getRequestURI());

        return "error/403";
    }

    @ExceptionHandler(ValidationException.class)
    public String handleValidation(ValidationException ex, Model model, HttpServletRequest request) {
        model.addAttribute("errorMessage", ex.getMessage());
        model.addAttribute("errorCode", "400");
        model.addAttribute("requestUrl", request.getRequestURI());

        return "error/400";
    }

    @ExceptionHandler(Exception.class)
    public String handleGenericException(Exception ex, Model model, HttpServletRequest request) {
        model.addAttribute("errorMessage", "Der opstod en uventet fejl. Prøv venligst igen.");
        model.addAttribute("errorCode", "500");
        model.addAttribute("requestUrl", request.getRequestURI());
        model.addAttribute("technicalMessage", ex.getMessage());

        //Log fejlen i terminal
        System.err.println("Uventet fejl: " + ex.getMessage());
        ex.printStackTrace();

        return "error/500";
    }
}
