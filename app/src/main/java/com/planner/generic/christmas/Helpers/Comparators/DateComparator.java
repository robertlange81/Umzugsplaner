package com.planner.generic.christmas.Helpers.Comparators;

import com.planner.generic.christmas.Activities.MainActivity;
import com.planner.generic.christmas.Model.Task;

public class DateComparator implements ComparatorSortable {

    private final boolean ignoreSpecialOptions;

    public DateComparator(boolean ignoreSpecialOptions) {
        this.ignoreSpecialOptions = ignoreSpecialOptions;
    }

    public DateComparator() {
        super();
        ignoreSpecialOptions = false;
    }

    @Override
    public int compare(Task o1, Task o2) {

        int cmpr = 0;

        if(!ignoreSpecialOptions && MainActivity.instance.getHideDoneTasksChecked()) {
            if(o1.is_Done && !o2.is_Done)
                cmpr = 1;
            if(!o1.is_Done && o2.is_Done)
                cmpr = -1;
        }

        if(!ignoreSpecialOptions && cmpr == 0 && MainActivity.instance.getHideNormalPrioTasksChecked()) {
            cmpr = o1.priority.compareTo(o2.priority) * -1;
        }

        if(cmpr != 0)
            return cmpr;

        if(o1.date == null && o2.date != null)
            return -1;
        if(o1.date != null && o2.date == null)
            return 1;

        if(o1.date != null)
            cmpr = o1.date.compareTo(o2.date);

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
        return ComparatorConfig.SortType.DATE;
    }
}
