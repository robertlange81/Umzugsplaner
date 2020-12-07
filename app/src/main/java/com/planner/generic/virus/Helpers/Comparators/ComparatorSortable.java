package com.planner.generic.virus.Helpers.Comparators;

import com.planner.generic.virus.Model.Task;

import java.util.Comparator;

public interface ComparatorSortable extends Comparator<Task> {
    ComparatorConfig.SortType getSortType();
}
