package org.example.projectplanningapp.exceptions;

public class UnauthorizedException extends RuntimeException{

    public UnauthorizedException(String message) {
        super(message);
    }

    public UnauthorizedException() {
        super("Du har ikke adgang til denne ressource");
    }

}
