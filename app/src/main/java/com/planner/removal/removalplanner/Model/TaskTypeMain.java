package com.planner.removal.removalplanner.Model;

public enum TaskTypeMain {
    OldFlat(0), NewFlat(1), Contracts(2), Movement(4);

    private int value;

    TaskTypeMain(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}