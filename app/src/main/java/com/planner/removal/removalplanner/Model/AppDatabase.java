package com.planner.removal.removalplanner.Model;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;

@Database(entities = {Task.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract TaskDao taskDao();

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {

        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE Task ADD COLUMN createdAt text");
        }
    };
}
