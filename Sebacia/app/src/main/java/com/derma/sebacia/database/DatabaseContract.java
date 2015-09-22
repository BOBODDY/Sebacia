package com.derma.sebacia.database;

import android.provider.BaseColumns;

/**
 * Created by nick on 9/15/15.
 */
public final class DatabaseContract {

    public DatabaseContract() {}

    public static abstract class PictureEntry implements BaseColumns {
        public static final String TABLE_NAME = "pictures";
        public static final String COLUMN_NAME_PATIENT = "patientId";
        public static final String COLUMN_NAME_PATH = "filePath";
        public static final String COLUMN_NAME_SEVERITY = "severity";
    }
}
