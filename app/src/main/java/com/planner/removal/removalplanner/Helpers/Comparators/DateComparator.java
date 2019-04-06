package com.planner.removal.removalplanner.Helpers.Comparators;

import com.planner.removal.removalplanner.Model.Task;

public class DateComparator implements ComparatorSortable {
    @Override
    public int compare(Task o1, Task o2) {

        if(o1.date == null && o2.date != null)
            return -1;
        if(o1.date != null && o2.date == null)
            return 1;
        if(o1.date == null && o2.date == null)
            return 0;

        return o1.date.compareTo(o2.date);
    }

    @Override
    public ComparatorConfig.SortType getSortType() {
        return ComparatorConfig.SortType.DATE;
    }
}
