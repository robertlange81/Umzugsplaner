package com.planner.virus.corona.Helpers.Comparators;

import com.planner.virus.corona.Model.Task;

import java.util.Comparator;

public interface ComparatorSortable extends Comparator<Task> {
    ComparatorConfig.SortType getSortType();
}
