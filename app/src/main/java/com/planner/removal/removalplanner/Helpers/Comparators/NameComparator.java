package com.planner.removal.removalplanner.Helpers.Comparators;

import com.planner.removal.removalplanner.Model.Task;

import java.util.Comparator;

public class NameComparator implements Comparator<Task> {
    @Override
    public int compare(Task o1, Task o2) {
        return o1.Name.compareTo(o2.Name);
    }
}
