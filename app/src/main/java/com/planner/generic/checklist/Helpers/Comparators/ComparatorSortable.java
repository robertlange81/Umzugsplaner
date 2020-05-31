package com.planner.generic.checklist.Helpers.Comparators;

import com.planner.generic.checklist.Model.Task;

import java.util.Comparator;

public interface ComparatorSortable extends Comparator<Task> {
    ComparatorConfig.SortType getSortType();
}
