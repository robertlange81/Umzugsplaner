package com.planner.generic.christmas.Helpers.Comparators;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ComparatorConfig {
    public static Map<SortType, ComparatorSortable> sortableMap;

    public ComparatorConfig() {
        sortableMap = GetComparators();
    }

    public enum SortType {

        NONE(0),
        COSTS(1),
        DATE(2),
        IS_DONE(3),
        NAME(4),
        PRIORITY(5),
        TYPE(6);

        private int value;

        SortType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public static Map<SortType, ComparatorSortable> GetComparators() {
        Map<SortType, ComparatorSortable> sortingComparator = new HashMap<SortType, ComparatorSortable>();

        sortingComparator.put(SortType.COSTS, new CostsComparator());
        sortingComparator.put(SortType.DATE, new DateComparator());
        sortingComparator.put(SortType.IS_DONE, new IsDoneComparator());
        sortingComparator.put(SortType.NAME, new NameComparator());
        sortingComparator.put(SortType.PRIORITY, new PriorityComparator());
        sortingComparator.put(SortType.TYPE, new TypeComparator());

        Collections.unmodifiableMap(sortingComparator);
        return sortingComparator;
    }
}
