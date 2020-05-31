package com.planner.virus.corona.Helpers.DBConverter;

import android.arch.persistence.room.TypeConverter;

import com.planner.virus.corona.Model.TaskType;


public class TaskTypeConverter {

    @TypeConverter
    public static TaskType fromInt(int value) {
        return new TaskType(value);
    }


    @TypeConverter
    public static int taskTypeToInt(TaskType value) {
        return value == null ? null : value.getValue();
    }
}