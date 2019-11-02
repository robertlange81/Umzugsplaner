package com.planner.removal.removalplanner.Model;

public enum TaskType {
    BATH(0), KITCHEN(1), LIVING_ROOM(2), BEDROOM(3), NURSERY(4), WORKROOM(5), ORGANISATION(6), TRANSPORT(7), SECURITY(8), MISCELLANEOUS(9);

    private int value;

    TaskType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
