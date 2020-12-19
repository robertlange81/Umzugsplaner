package com.planner.generic.baby.Helpers.Comparators;

import com.planner.generic.baby.Model.Task;

import java.util.Comparator;

public interface ComparatorSortable extends Comparator<Task> {
    ComparatorConfig.SortType getSortType();
}
