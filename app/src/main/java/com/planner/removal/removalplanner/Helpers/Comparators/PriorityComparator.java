package com.planner.removal.removalplanner.Helpers.Comparators;

import com.planner.removal.removalplanner.Model.Task;

import java.util.Comparator;

public class PriorityComparator implements Comparator<Task>, ASortable {
    @Override
    public int compare(Task o1, Task o2) {
        return o1.Priority.compareTo(o2.Priority);
    }

    @Override
    public ComparatorConfig.SortType getSortType() {
        return ComparatorConfig.SortType.PRIORITY;
    }
}
