package com.planner.removal.removalplanner.Helpers.Comparators;

import com.planner.removal.removalplanner.Model.Task;

import java.util.Comparator;

public class IsDoneComparator implements Comparator<Task>, ASortable {
    @Override
    public int compare(Task o1, Task o2) {
        if(o1.Is_Done && !o2.Is_Done)
            return 1;
        if(!o1.Is_Done && o2.Is_Done)
            return -1;

        return 0;
    }

    @Override
    public ComparatorConfig.SortType getSortType() {
        return ComparatorConfig.SortType.IS_DONE;
    }
}
