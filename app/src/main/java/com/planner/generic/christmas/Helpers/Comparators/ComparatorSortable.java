package com.planner.generic.christmas.Helpers.Comparators;

import com.planner.generic.christmas.Model.Task;

import java.util.Comparator;

public interface ComparatorSortable extends Comparator<Task> {
    ComparatorConfig.SortType getSortType();
}
