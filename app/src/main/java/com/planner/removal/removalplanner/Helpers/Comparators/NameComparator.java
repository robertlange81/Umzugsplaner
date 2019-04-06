package com.planner.removal.removalplanner.Helpers.Comparators;

import com.planner.removal.removalplanner.Model.Task;

public class NameComparator implements ComparatorSortable {
    @Override
    public int compare(Task o1, Task o2) {
        return o1.name.compareTo(o2.name);
    }

    @Override
    public ComparatorConfig.SortType getSortType() {
        return ComparatorConfig.SortType.NAME;
    }
}
