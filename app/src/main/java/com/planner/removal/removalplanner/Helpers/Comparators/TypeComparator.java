package com.planner.removal.removalplanner.Helpers.Comparators;

import com.planner.removal.removalplanner.Model.Task;

import java.util.Comparator;

public class TypeComparator implements Comparator<Task> {
    @Override
    public int compare(Task o1, Task o2) {
        return o1.Type.compareTo(o2.Type);
    }
}
