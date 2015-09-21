package com.derma.sebacia.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.derma.sebacia.data.AcneLevel;
import com.derma.sebacia.data.Doctor;
import com.derma.sebacia.data.Patient;
import com.derma.sebacia.data.Picture;

import com.derma.sebacia.database.DatabaseContract.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by nick on 9/15/15.
 */
public class LocalDb implements databaseInterface {

    DatabaseOpenHelper dbHelper;
    Context context;
    
    private final String TAG = "LocalDb";
    
    public LocalDb(Context ctx) {
        context = ctx;
        initializeDbHelper();
    }

    private void initializeDbHelper() {
        if(context != null) {
            dbHelper = new DatabaseOpenHelper(context);
        }
    }

    //GETS
    public Picture getPicture(int PatientID, String FilePath){
        if(dbHelper == null) {
            initializeDbHelper();
        }

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        
        String[] projection = {
                PictureEntry._ID,
                PictureEntry.COLUMN_NAME_PATIENT,
                PictureEntry.COLUMN_NAME_SEVERITY,
                PictureEntry.COLUMN_NAME_PATH
        };
        String sortOrder = PictureEntry.COLUMN_NAME_PATIENT + " DESC";
        
        String selection = PictureEntry.COLUMN_NAME_PATH; //Columns for WHERE clause
        String[] selectionArgs = {FilePath}; // Values for WHERE clause
        
        Cursor c = db.query(PictureEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);
        
        c.moveToFirst();
        String filePath = c.getString(c.getColumnIndexOrThrow(PictureEntry.COLUMN_NAME_PATH));
        int sev = c.getInt(c.getColumnIndexOrThrow(PictureEntry.COLUMN_NAME_SEVERITY));
        
        c.close();

        File f = new File(filePath);
        byte[] bytes = null;
        
        try {
            FileInputStream fis = new FileInputStream(f);
            bytes = new byte[fis.available()];
            
            int readRes = fis.read(bytes);
        } catch(FileNotFoundException fnfe) {
            Log.e("LocalDb", "file not found", fnfe);
        } catch(IOException ioe) {
            Log.e("LocalDb", "io exception", ioe);
        }
        
        if(bytes != null) {
            return new Picture(filePath, new AcneLevel(sev, sev + ""), bytes);
        }
        
        return null;
    }

    public List<Picture> getPatientPics(Patient patient){
        return null;
    }

    public List<Doctor> getDoctors(AcneLevel Severity){
        return null;
    }

    public Doctor getDoctor(int DocID){
        return null;
    }

    public Patient getPatient(Patient patient){
        return null;
    }

    //POSTS
    public boolean addPatient(Patient patient) {
        return false;
    }

    public boolean addDoctor(Doctor doctor) {
        return false;
    }

    public boolean addPicture(Picture picture) {
        if(dbHelper == null)
            initializeDbHelper();
        
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        
        try {
            FileOutputStream fos = context.openFileOutput(picture.getFilePath(), Context.MODE_PRIVATE);
            fos.write(picture.getPic());
            fos.close();
        } catch(FileNotFoundException fnfe) {
            Log.e(TAG, "file not found writing picture", fnfe);
            return false;
        } catch(IOException ioe) {
            Log.e(TAG, "io exception writing picture", ioe);
            return false;
        }

        ContentValues values = new ContentValues();
        values.put(PictureEntry.COLUMN_NAME_PATH, picture.getFilePath());
        values.put(PictureEntry.COLUMN_NAME_SEVERITY, picture.getSeverity().getLevel());
        
        long newRowId = db.insert(PictureEntry.TABLE_NAME, null, values);
        
        db.close();
        
        return newRowId != -1;
    }
}
