package com.planner.generic.christmas.Model;

public enum TaskTypeChristmas implements TaskTypeEnumHelper {
    Bureaucracy(0), Presents(1), Food(2), Drinks(3), Friends(4), Decoration(5), Miscellaneous(6);

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
