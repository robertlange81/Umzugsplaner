package com.planner.generic.baby.Model;

public enum TaskTypeBaby implements TaskTypeEnumHelper {
    Bureaucracy(0), Clothes(1), Food(2), Furnishings(3), Literature(4), Medicine(5), Toys(6), Other(7);

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
