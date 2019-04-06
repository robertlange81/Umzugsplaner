package com.planner.removal.removalplanner.Helpers.Comparators;

import com.planner.removal.removalplanner.Model.Task;

public class PriorityComparator implements ComparatorSortable {
    @Override
    public int compare(Task o1, Task o2) {
        return o1.priority.compareTo(o2.priority) * -1;
    }

    @Override
    public ComparatorConfig.SortType getSortType() {
        return ComparatorConfig.SortType.PRIORITY;
    }
}
