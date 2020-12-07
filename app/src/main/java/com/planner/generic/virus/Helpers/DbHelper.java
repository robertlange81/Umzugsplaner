package com.planner.generic.virus.Helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import com.planner.generic.virus.Model.Task;

public class DbHelper extends SQLiteOpenHelper {
    private static final String _DB_FILE_NAME = "Planner";
    private static final int _DB_VERSION = 4;

    public DbHelper(@Nullable Context context) {
        super(context, _DB_FILE_NAME, null, _DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
