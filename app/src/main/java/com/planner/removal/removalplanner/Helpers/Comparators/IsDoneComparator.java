package com.planner.removal.removalplanner.Helpers.Comparators;

import com.planner.removal.removalplanner.Model.Task;

public class IsDoneComparator implements ComparatorSortable {
    @Override
    public int compare(Task o1, Task o2) {
        if(o1.is_Done && !o2.is_Done)
            return -1;
        if(!o1.is_Done && o2.is_Done)
            return 1;

        return 0;
    }

    @Override
    public ComparatorConfig.SortType getSortType() {
        return ComparatorConfig.SortType.IS_DONE;
    }
}
