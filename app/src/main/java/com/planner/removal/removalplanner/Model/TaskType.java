package com.planner.removal.removalplanner.Model;

public class TaskType implements Comparable<Object>{

    private int value;

    public TaskType(int value) {
        this.value = value;
    }

    public TaskType(TaskTypeMain base) {
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

        if(o instanceof TaskTypeMain)
            other = ((TaskTypeMain) o).getValue();

        if(o.getClass() == int.class)
            other = (int) o;

        return Integer.valueOf(this.getValue()).compareTo(other);
    }
}

