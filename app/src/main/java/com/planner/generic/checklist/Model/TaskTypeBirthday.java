package com.planner.generic.checklist.Model;

public enum TaskTypeBirthday implements TaskTypeEnumHelper {
    Bureaucracy(0), Presents(1), Catering(2), Friends(3), Miscellaneous(4);

    private int value;

    TaskTypeBirthday(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String getNameByCode(int code) {
        for(TaskTypeBirthday e : TaskTypeBirthday.values()){
            if(code == e.value) return e.name();
        }
        return null;
    }
}
