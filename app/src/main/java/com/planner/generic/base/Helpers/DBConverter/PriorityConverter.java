package com.planner.generic.base.Helpers.DBConverter;

import android.arch.persistence.room.TypeConverter;

import com.planner.generic.base.Model.Priority;

import java.text.DateFormat;
import java.text.SimpleDateFormat;


public class PriorityConverter {

    private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @TypeConverter
    public static Priority fromTimestamp(int value) {
        if(value == 0)
            return Priority.Normal;

        return Priority.High;
    }


    @TypeConverter
    public static int dateToTimestamp(Priority value) {
        return value == null ? null : value.getValue();
    }
}