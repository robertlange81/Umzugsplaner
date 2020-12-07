package com.planner.generic.virus.Model;

public enum TaskTypeBase implements TaskTypeEnumHelper {
    Simple(0), Medium(1), Difficult(2);

    private int value;

    TaskTypeBase(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String getNameByCode(int code) {
        for(TaskTypeBase e : TaskTypeBase.values()){
            if(code == e.value) return e.name();
        }
        return null;
    }
}
