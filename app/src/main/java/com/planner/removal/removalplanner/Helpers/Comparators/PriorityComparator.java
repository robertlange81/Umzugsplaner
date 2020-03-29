package com.planner.removal.removalplanner.Helpers.Comparators;

import com.planner.removal.removalplanner.Activities.MainActivity;
import com.planner.removal.removalplanner.Model.Task;

public class PriorityComparator implements ComparatorSortable {
    @Override
    public int compare(Task o1, Task o2) {
        // first by priority
        int cmpr = 0;

        if(MainActivity.instance.getHideDoneTasksChecked()) {
            if(o1.is_Done && !o2.is_Done)
                cmpr = 1;
            if(!o1.is_Done && o2.is_Done)
                cmpr = -1;
        }

        if(cmpr == 0)
            cmpr = o1.priority.compareTo(o2.priority) * -1;

        // then by name
        if(cmpr == 0)
            cmpr = o1.name.compareToIgnoreCase(o2.name);

        // then by createdAt
        if(cmpr == 0)
            cmpr = o1.createdAt.compareTo(o2.createdAt);

        return cmpr;
    }

    @Override
    public ComparatorConfig.SortType getSortType() {
        return ComparatorConfig.SortType.PRIORITY;
    }
}
