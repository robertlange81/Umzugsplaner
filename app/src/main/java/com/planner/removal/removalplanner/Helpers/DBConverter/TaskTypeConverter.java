package com.planner.removal.removalplanner.Helpers.DBConverter;

import android.arch.persistence.room.TypeConverter;
import com.planner.removal.removalplanner.Model.TaskType;


public class TaskTypeConverter {

    @TypeConverter
    public static TaskType fromInt(int value) {
        switch (value) {
            case 0:
                return TaskType.BATH;
            case 1:
                return TaskType.KITCHEN;
            case 2:
                return TaskType.LIVING_ROOM;
            case 3:
                return TaskType.BEDROOM;
            case 4:
                return TaskType.NURSERY;
            case 5:
                return TaskType.WORKROOM;
            case 6:
                return TaskType.ORGANISATION;
            case 7:
                return TaskType.TRANSPORT;
            default:
                return TaskType.MISCELLANEOUS;
        }
    }


    @TypeConverter
    public static int taskTypeToInt(TaskType value) {
        return value == null ? null : value.getValue();
    }
}