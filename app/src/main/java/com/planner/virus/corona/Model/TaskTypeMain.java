package com.planner.virus.corona.Model;

public enum TaskTypeMain {
    OldFlat(0), NewFlat(1), Contracts(2), Movement(3), Electronics(4), Furniture(5), KITCHEN(6);

    private int value;

    TaskTypeMain(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
