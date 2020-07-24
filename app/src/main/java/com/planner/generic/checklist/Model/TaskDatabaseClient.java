package com.planner.generic.checklist.Model;

import android.arch.persistence.room.Room;
import android.content.Context;

public class TaskDatabaseClient {

    private Context mCtx;
    private static TaskDatabaseClient mInstance;

    //our app database object
    private TaskDatabase taskDatabase;

    private TaskDatabaseClient(Context mCtx) {
        this.mCtx = mCtx;

        // creating the app database with Room database builder
        // Planner is the name of the database
        taskDatabase = Room.databaseBuilder(mCtx, TaskDatabase.class, "Planner")
                //.fallbackToDestructiveMigration()
                .addMigrations(TaskDatabase.MIGRATION_1_2)
                .addMigrations(TaskDatabase.MIGRATION_2_3)
                .build();
    }

    public static synchronized TaskDatabaseClient getInstance(Context mCtx) {
        if (mInstance == null) {
            mInstance = new TaskDatabaseClient(mCtx);
        }
        return mInstance;
    }

    public TaskDatabase getTaskDatabase() {
        return taskDatabase;
    }
}
