package com.planner.removal.removalplanner.Helpers.Comparators;

import com.planner.removal.removalplanner.Model.Task;

public class TypeComparator implements ComparatorSortable {
    @Override
    public int compare(Task o1, Task o2) {
        return o1.type.compareTo(o2.type);
    }

    @Override
    public ComparatorConfig.SortType getSortType() {
        return ComparatorConfig.SortType.TYPE;
    }
}
