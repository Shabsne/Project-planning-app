package org.example.projectplanningapp.models;

public enum Role {
    ADMIN("Admin", 1),
    DEVELOPER("Udvikler", 2);

    private final String displayName;
    private final int id;

    Role(String displayName, int id) {
        this.displayName = displayName;
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getId() {
        return id;
    }

    public static Role fromId(int id) {
        for (Role r : Role.values()) {
            if (r.getId() == id) {
                return r;
            }
        }
        return null;
    }
}

