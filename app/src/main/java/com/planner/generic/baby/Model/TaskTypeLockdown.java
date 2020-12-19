package com.planner.generic.baby.Model;

public enum TaskTypeLockdown implements TaskTypeEnumHelper {
    FOOD(0), MEDICIN(1), HOUSEHOLD(2), ELECTRONICS(3), MISCELLANEOUS(4);

    private int value;

    TaskTypeLockdown(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String getNameByCode(int code) {
        for(TaskTypeLockdown e : TaskTypeLockdown.values()){
            if(code == e.value) return e.name();
        }
        return null;
    }
}
