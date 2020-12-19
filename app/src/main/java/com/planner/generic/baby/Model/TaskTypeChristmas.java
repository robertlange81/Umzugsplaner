package com.planner.generic.baby.Model;

public enum TaskTypeChristmas implements TaskTypeEnumHelper {
    Bureaucracy(0), Presents(1), Food(2), Entertainment(3), Decoration(4), Miscellaneous(5);

    private int value;

    TaskTypeChristmas(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String getNameByCode(int code) {
        for(TaskTypeChristmas e : TaskTypeChristmas.values()){
            if(code == e.value) return e.name();
        }
        return null;
    }
}
