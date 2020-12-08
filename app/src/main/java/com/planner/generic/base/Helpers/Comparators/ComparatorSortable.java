package com.planner.generic.base.Helpers.Comparators;

import com.planner.generic.base.Model.Task;

import java.util.Comparator;

public interface ComparatorSortable extends Comparator<Task> {
    ComparatorConfig.SortType getSortType();
}
