package com.planner.generic.checklist.Model;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public final class TaskContract {
    /**
     * Eindeutiger Name des Providers innerhalb des Betriebssystems
     */
    public static final String AUTHORITY = "com.planner.generic.checklist.providerx";

    /**
     * Basis URI zum Content Provider
     */
    public static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);

    /**
     * Kontrakt für zeiten
     */
    public static final class TaskData {
        /**
         * Unterverzeichnis für die Daten
         */
        public static final String CONTENT_DIRECTORY = "planner";


        /**
         * URI zu den Daten
         */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, CONTENT_DIRECTORY);


        /**
         * Datentyp für die Auflistung der Daten
         */
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
                + "/" + CONTENT_DIRECTORY;

        /**
         * Datentyp für einen einzelnen Datensatz
         */
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
                + "/" + CONTENT_DIRECTORY;

        public static long idForOnlyOneItem = 100;

        /**
         * Verfügbare Splaten
         */
        public interface Columns {
            String _id      = "id";
            String _costs      = "id";
            String _createdAt      = "id";
            String _date      = "id";
            String _description      = "id";
            String _is_Done      = "id";
            String _links      = "id";
            String _locationPlace      = "id";
            String _locationStreet      = "id";
            String _locationStreetNumber      = "id";
            String _locationZip      = "id";
            String _name    = "name";
            String _priority    = "name";
            String _type    = "name";
        }
    }
}
