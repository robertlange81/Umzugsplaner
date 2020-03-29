package com.planner.removal.removalplanner.Model;

import android.arch.persistence.room.Room;
import android.content.Context;

public class TaskDatabaseClient {

    private Context mCtx;
    private static TaskDatabaseClient mInstance;

    //our app database object
    private AppDatabase appDatabase;

    private TaskDatabaseClient(Context mCtx) {
        this.mCtx = mCtx;

        // creating the app database with Room database builder
        // RemovalPlanner is the name of the database
        appDatabase = Room.databaseBuilder(mCtx, AppDatabase.class, "RemovalPlanner")
                //.fallbackToDestructiveMigration()
                .addMigrations(AppDatabase.MIGRATION_1_2)
                .build();
    }

    public static synchronized TaskDatabaseClient getInstance(Context mCtx) {
        if (mInstance == null) {
            mInstance = new TaskDatabaseClient(mCtx);
        }
        return mInstance;
    }

    public AppDatabase getAppDatabase() {

        return appDatabase;
    }
}
