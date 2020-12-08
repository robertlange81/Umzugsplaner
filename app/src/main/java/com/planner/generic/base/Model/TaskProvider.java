package com.planner.generic.base.Model;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.planner.generic.base.Helpers.DbHelper;

import java.util.Locale;

public class TaskProvider extends ContentProvider {
    private static final UriMatcher _uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // https://medium.com/@rash.3692/example-of-room-database-with-content-provider-part1-23b93646e09f

    static {
        // Lookup für die Auflistung
        _uriMatcher.addURI(
                TaskContract.AUTHORITY, // Basis-Uri
                TaskContract.TaskData.CONTENT_DIRECTORY, // Unterverzeichnis der Daten
                Task.ITEM_LIST_ID); // Eindeutige ID

        // Lookup für ein Datensatz
        _uriMatcher.addURI(
                TaskContract.AUTHORITY, // Basis-Uri
                TaskContract.TaskData.CONTENT_DIRECTORY + "/#", // Unterverzeichnis mit ID des Datensatzes
                Task.ITEM_ID); // Eindeutige ID
    }

    // private TaskDao taskDao;

    public static final String TAG = TaskProvider.class.getName();
    private DbHelper _dbHelper = null;

    @Override
    public boolean onCreate() {
        //taskDao = TaskDatabaseClient.getInstance(getContext()).getTaskDatabase().getTaskDao();

        _dbHelper = new DbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor data;
        SQLiteDatabase db = _dbHelper.getReadableDatabase();

        switch ( _uriMatcher.match(uri)) {
            case Task.ITEM_LIST_ID:
                data = db.query(Task.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;

            case Task.ITEM_ID:
                data = db.query(Task.TABLE_NAME, projection, selection, selectionArgs, null, null, null);
                break;

            default:
                throw new IllegalArgumentException(String.format(Locale.GERMANY, "Unbekannte URI: %s", uri));
        }

        if (data != null) {
            data.setNotificationUri(getContext().getContentResolver(), uri);
        }

        return data;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        // Auflösen der Uri
        final int uriType = _uriMatcher.match(uri);
        String type = null;

        // Bestimmen des Datentyps
        switch (uriType) {
            case Task.ITEM_LIST_ID:
                type = TaskContract.TaskData.CONTENT_TYPE;
                break;

            case Task.ITEM_ID:
                type = TaskContract.TaskData.CONTENT_ITEM_TYPE;
                break;
        }

        return type;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    /**
     * Konvertierung einer long ID in ein String Array
     *
     * @param id ID
     * @return ID als String Array
     */
    private String[] idAsArray(String id) {
        return new String[]{String.valueOf(id)};
    }
}

