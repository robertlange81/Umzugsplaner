package com.planner.removal.removalplanner.Helpers.Comparators;

import com.planner.removal.removalplanner.Model.Task;

import java.util.Comparator;

public class IsDoneComparator implements Comparator<Task> {
    @Override
    public int compare(Task o1, Task o2) {
        if(o1.IsDone && !o2.IsDone)
            return 1;
        if(!o1.IsDone && o2.IsDone)
            return -1;

        return 0;
    }
}
