package com.planner.generic.virus.Model;

import java.io.Serializable;

public class TaskType implements Comparable<Object>, Serializable {

    private int value;

    public TaskType(int value) {
        this.value = value;
    }

    public TaskType(TaskTypeBase base) {
        this.value = base.getValue();
    }

    public TaskType(TaskTypeBirthday base) {
        this.value = base.getValue();
    }

    public TaskType(TaskTypeChristmas base) {
        this.value = base.getValue();
    }

    public TaskType(TaskTypeLockdown base) {
        this.value = base.getValue();
    }

    public TaskType(TaskTypeRelocation base) {
        this.value = base.getValue();
    }

    public TaskType(TaskTypeWedding base) {
        this.value = base.getValue();
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        return this.compareTo(o) == 0;
    }

    @Override
    public int hashCode() {
        return getValue();
    }

    @Override
    public int compareTo(Object o) {

        if(o == null)
            return 1;

        int other = 0;
        if(o.getClass() == TaskType.class)
            other = ((TaskType) o).getValue();

        /* Should not be necessary
        if(o instanceof TaskTypeBase)
            other = ((TaskTypeBase) o).getValue();
        else if(o instanceof TaskTypeBaby)
            other = ((TaskTypeBaby) o).getValue();
        else if(o instanceof TaskTypeBirthday)
            other = ((TaskTypeBirthday) o).getValue();
        else if(o instanceof TaskTypeChristmas)
            other = ((TaskTypeChristmas) o).getValue();
        else if(o instanceof TaskTypeLockdown)
            other = ((TaskTypeLockdown) o).getValue();
        else if(o instanceof TaskTypeRelocation)
            other = ((TaskTypeRelocation) o).getValue();
        else if(o instanceof TaskTypeWedding)
            other = ((TaskTypeWedding) o).getValue();
         */

        if(o.getClass() == int.class)
            other = (int) o;

        return Integer.valueOf(this.getValue()).compareTo(other);
    }
}

