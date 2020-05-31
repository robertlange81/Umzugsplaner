package com.planner.virus.corona.Model;

public enum Priority {
    Normal(0), High(1);

    private int value;

    Priority(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
