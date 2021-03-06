package com.derma.sebacia.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.nfc.Tag;
import android.util.Log;

import com.derma.sebacia.database.DatabaseContract.*;

/**
 * Helper class for creating and maintaining a database
 */
public class DatabaseOpenHelper extends SQLiteOpenHelper {
    
    private static final String TAG = "Sebacia";

    private static final String DB_NAME = "pictures";
    private static final int DB_VERSION = 3;

    private static final String PICTURE_TABLE_CREATE =
            "CREATE TABLE " + PictureEntry.TABLE_NAME + " (" +
                    PictureEntry._ID + " INTEGER PRIMARY KEY, " +
                    PictureEntry.COLUMN_NAME_PATIENT + " INTEGER, " +
                    PictureEntry.COLUMN_NAME_SEVERITY + " TEXT, " +
                    PictureEntry.COLUMN_NAME_PATH + ");";
    private static final String PICTURE_TABLE_DELETE = "" +
            "DROP TABLE IF EXISTS " + PictureEntry.TABLE_NAME;

    public DatabaseOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        Log.d(TAG, "in Database helper constructor");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "creating database");
        db.execSQL(PICTURE_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //This deletes data, should figure out a proper upgrade path
        Log.d(TAG, "upgrading database");
        db.execSQL(PICTURE_TABLE_DELETE);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "downgrading database");
        onUpgrade(db, oldVersion, newVersion);
    }
}
