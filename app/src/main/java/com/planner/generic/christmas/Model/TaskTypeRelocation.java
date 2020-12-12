package com.planner.generic.christmas.Model;

public enum TaskTypeRelocation implements TaskTypeEnumHelper {
    OldFlat(0), NewFlat(1), Contracts(2), Movement(3), Electronics(4), Furniture(5), KITCHEN(6);

    private int value;

    TaskTypeRelocation(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String getNameByCode(int code) {
        for(TaskTypeRelocation e : TaskTypeRelocation.values()){
            if(code == e.value) return e.name();
        }
        return null;
    }
}
