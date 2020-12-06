package com.planner.generic.checklist.Model;

public enum TaskTypeWedding implements TaskTypeEnumHelper {
    Bureaucracy(0), Location(1), Catering(2), FriendsOrFamily(3), Clothes(4), Miscellaneous(5);

    private int value;

    TaskTypeWedding(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String getNameByCode(int code) {
        for(TaskTypeWedding e : TaskTypeWedding.values()){
            if(code == e.value) return e.name();
        }
        return null;
    }
}
