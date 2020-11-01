package com.planner.generic.checklist.Model;

public enum TaskTypeLockdown {
    //OldFlat(0), NewFlat(1), Contracts(2), Movement(3), Electronics(4), Furniture(5), KITCHEN(6);
    FOOD(0), MEDICIN(1), HOUSEHOLD(2), ELECTRONICS(3), MISCELLANEOUS(4);

    private int value;

    TaskTypeLockdown(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
