package com.planner.removal.removalplanner.Helpers.Comparators;

import com.planner.removal.removalplanner.Model.Task;
import java.util.Comparator;

public class DateComparator implements Comparator<Task>, ASortable {
    @Override
    public int compare(Task o1, Task o2) {

        if(o1.Date == null && o2.Date != null)
            return -1;
        if(o1.Date != null && o2.Date == null)
            return 1;
        if(o1.Date == null && o2.Date == null)
            return 0;

        return o1.Date.compareTo(o2.Date);
    }

    @Override
    public ComparatorConfig.SortType getSortType() {
        return ComparatorConfig.SortType.DATE;
    }
}
