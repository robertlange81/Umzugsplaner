package com.planner.removal.removalplanner.Helpers.DBConverter;

import android.arch.persistence.room.TypeConverter;

import com.planner.removal.removalplanner.Model.TaskType;

import java.util.UUID;


public class UuidConverter {

    @TypeConverter
    public static UUID fromString(String value) {
        try {
            return UUID.fromString(value);
        } catch (Exception e) {}

        return UUID.randomUUID();
    }


    @TypeConverter
    public static String UuidToString(UUID value) {
        return value.toString();
    }
}