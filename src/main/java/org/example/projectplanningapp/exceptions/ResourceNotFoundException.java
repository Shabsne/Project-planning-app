package org.example.projectplanningapp.exceptions;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super (message);
    }

    public ResourceNotFoundException(String resource, int id) {
        super(resource + " med ID " + id + " blev ikke fundet");
    }
}
