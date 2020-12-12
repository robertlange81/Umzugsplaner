package com.planner.generic.christmas.Helpers.Comparators;

import com.planner.generic.christmas.Activities.MainActivity;
import com.planner.generic.christmas.Model.Task;

public class TypeComparator implements ComparatorSortable {
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
            cmpr = new Integer(o1.type.getValue()).compareTo(o2.type.getValue());

        if(cmpr != 0)
            return cmpr;

        // then by date
        if(o1.date == null && o2.date != null)
            return -1;
        if(o1.date != null && o2.date == null)
            return 1;

        if(o1.date != null)
            cmpr = o1.date.compareTo(o2.date);

        // then by name
        if(cmpr == 0)
            cmpr = o1.name.compareToIgnoreCase(o2.name);

        return cmpr;
    }

    @Override
    public ComparatorConfig.SortType getSortType() {
        return ComparatorConfig.SortType.PRIORITY;
    }
}
