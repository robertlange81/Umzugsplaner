package com.planner.generic.checklist.Model;

public enum TaskTypeRelocation {
    OldFlat(0), NewFlat(1), Contracts(2), Movement(3), Electronics(4), Furniture(5), KITCHEN(6);

    private int value;

    TaskTypeRelocation(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
