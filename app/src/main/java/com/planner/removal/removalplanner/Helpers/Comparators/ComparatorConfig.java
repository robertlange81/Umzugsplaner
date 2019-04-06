package com.planner.removal.removalplanner.Helpers.Comparators;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ComparatorConfig {
    public static Map<SortType, ASortable> sortables;

    public ComparatorConfig() throws Exception {
        sortables = GetComparators();
        for (SortType sortType : SortType.values()) {
            ASortable s = sortables.get(sortType);
            if(!s.getSortType().equals(sortType)) {
                throw new Exception("Missing Comparator for Sort Type");
            }
        }

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

    public static Map<SortType, ASortable> GetComparators() {
        Map<SortType, ASortable> sortingComparator = new HashMap<SortType, ASortable>();

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
