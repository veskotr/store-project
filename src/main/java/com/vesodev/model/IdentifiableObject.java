package com.vesodev.model;

public abstract class IdentifiableObject {
    private final int id;

    public IdentifiableObject(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
