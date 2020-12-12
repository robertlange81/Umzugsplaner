package com.planner.generic.christmas.Model;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;

@Database(entities = {Task.class}, version = 4, exportSchema = false)
public abstract class TaskDatabase extends RoomDatabase {
    public abstract TaskDao getTaskDao();

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {

        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE Task ADD COLUMN createdAt text");
        }
    };

    static final Migration MIGRATION_2_3 = new Migration(2, 3) {

        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE Task ADD COLUMN locationZip text");
            database.execSQL("ALTER TABLE Task ADD COLUMN locationPlace text");
            database.execSQL("ALTER TABLE Task ADD COLUMN locationStreet text");
            database.execSQL("ALTER TABLE Task ADD COLUMN locationStreetNumber text");
        }
    };

    /* HACK: Problem with Cursor based provider changing db version to 4 - interacting with ROOM? */
    static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        public void migrate(SupportSQLiteDatabase database) {
        }
    };

    /* HACK: Problem with Cursor based provider changing db version to 4 - interacting with ROOM? */
    static final Migration MIGRATION_4_3 = new Migration(4, 3) {
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("UPDATE Task SET locationZip = 'not existing' WHERE locationZip = 'not existing'");
        }
    };
}
