package com.planner.generic.virus.Helpers.Comparators;

import com.planner.generic.virus.Activities.MainActivity;
import com.planner.generic.virus.Model.Task;

public class NameComparator implements ComparatorSortable {
    @Override
    public int compare(Task o1, Task o2) {

        int cmpr = 0;

        if(MainActivity.instance.getHideDoneTasksChecked()) {
            if(o1.is_Done && !o2.is_Done)
                cmpr = 1;
            if(!o1.is_Done && o2.is_Done)
                cmpr = -1;
        }

        if(cmpr == 0 && MainActivity.instance.getHideNormalPrioTasksChecked()) {
            cmpr = o1.priority.compareTo(o2.priority) * -1;
        }

        if(cmpr == 0)
            cmpr = o1.name.compareToIgnoreCase(o2.name);

        if(cmpr != 0)
            return cmpr;

        // then by date
        if(o1.date == null && o2.date != null)
            return -1;
        if(o1.date != null && o2.date == null)
            return 1;

        if(o1.date != null)
            cmpr = o1.date.compareTo(o2.date);

        // then by createdAt
        if(cmpr == 0)
            cmpr = o1.createdAt.compareTo(o2.createdAt);

        return cmpr;
    }

    @Override
    public ComparatorConfig.SortType getSortType() {
        return ComparatorConfig.SortType.NAME;
    }
}