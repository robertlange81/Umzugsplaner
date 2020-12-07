package com.planner.generic.virus.Model;

public enum TaskTypeBaby implements TaskTypeEnumHelper {
    Bureaucracy(0), Medicin(1), Food(2), Toys(3), Furniture(4), Clothes(5), Miscellaneous(6);

    private int value;

    TaskTypeBaby(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String getNameByCode(int code) {
        for(TaskTypeBaby e : TaskTypeBaby.values()){
            if(code == e.value) return e.name();
        }
        return null;
    }
}
