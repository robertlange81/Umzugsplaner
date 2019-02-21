package com.planner.removal.removalplanner.Model;

public enum TaskType {
    Organization(0), Furniture(1), Electronics(2), Transportation(3);

    private int value;

    TaskType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
