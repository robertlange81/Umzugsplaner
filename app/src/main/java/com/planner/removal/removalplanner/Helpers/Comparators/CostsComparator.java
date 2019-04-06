package com.planner.removal.removalplanner.Helpers.Comparators;

import com.planner.removal.removalplanner.Model.Task;


public class CostsComparator implements ComparatorSortable {
    @Override
    public int compare(Task o1, Task o2) {
        return -1 * o1.costs.compareTo(o2.costs);
    }

    @Override
    public ComparatorConfig.SortType getSortType() {
        return ComparatorConfig.SortType.COSTS;
    }
}
