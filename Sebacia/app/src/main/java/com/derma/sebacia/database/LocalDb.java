package com.derma.sebacia.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.derma.sebacia.R;
import com.derma.sebacia.data.AcneLevel;
import com.derma.sebacia.data.Doctor;
import com.derma.sebacia.data.Patient;
import com.derma.sebacia.data.Picture;

import com.derma.sebacia.database.DatabaseContract.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by nick on 9/15/15.
 * Local database implementation
 *
 */
public class LocalDb implements databaseInterface {

    DatabaseOpenHelper dbHelper;
    Context context;
    final int numQuestions = 6;  // TODO : make this a constant somewhere in the application
    //        because it corresponds to the number of acne classifications
    //        and other classes will make use of this value
    
    private final String TAG = "Sebacia";
    
    public LocalDb(Context ctx) {
        context = ctx;
        initializeDbHelper();
    }

    private void initializeDbHelper() {
        Log.d(TAG, "initializing db helper");
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
            return new Picture(filePath, new AcneLevel(sev, sev + ""));
        }
        
        return null;
    }

    public List<Picture> getPatientPics(Patient patient){
        List<Picture> pics = new ArrayList<>();
        int patientId = patient.getPatientID();
        
        if(dbHelper == null) {
            initializeDbHelper();
        }
        
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // The names of the columns we will use from the database
        String[] projection = {
                PictureEntry._ID,
                PictureEntry.COLUMN_NAME_PATIENT,
                PictureEntry.COLUMN_NAME_PATH,
                PictureEntry.COLUMN_NAME_SEVERITY
        };
        
        String sortOrder = PictureEntry.COLUMN_NAME_PATH + " DESC";
        String where = PictureEntry.COLUMN_NAME_PATIENT + "=?"; // Statement for the WHERE clause
        String[] whereArgs = {patientId + ""};     // What the WHERE clause should equal to be returned
        
        Cursor c = db.query(
                PictureEntry.TABLE_NAME,
                projection,
                where,
                whereArgs,
                null,
                null,
                sortOrder
        );
        
        c.moveToFirst();
        
        if(c.getCount() > 0) {
            Log.d(TAG, "there are some lines");
            do {
                String path = c.getString(c.getColumnIndexOrThrow(PictureEntry.COLUMN_NAME_PATH));
                String sev = c.getString(c.getColumnIndexOrThrow(PictureEntry.COLUMN_NAME_SEVERITY));

                pics.add(new Picture(path, new AcneLevel(Integer.valueOf(sev), sev)));

            } while (c.moveToNext());
        } else {
            Log.e(TAG, "no lines received from database");
        }
        
        c.close();
        
        return pics;
    }

    public List<Doctor> getDoctors(AcneLevel Severity){
        List<Doctor> doctors = new ArrayList<>();
        doctors.add(new Doctor("Cory Anderson", "2905 Premiere Pkwy, Duluth, GA 30097", 34, -84.5, 0, 0, 4));

        return doctors;
    }

    public Doctor getDoctor(int DocID){
        return null;
    }

    public Patient getPatient(Patient patient){
        return null;
    }

    public Iterator<Integer>[] getSurveyPictures() {
        Iterator<Integer>[] surveyPictures = new Iterator[numQuestions];

        // TODO: Consider pulling these from a DB
        Set<Integer> grade0 = new HashSet<>();
        grade0.add(R.drawable.grade0);

        Set<Integer> grade1 = new HashSet<>();
        grade1.add(R.drawable.grade1);

        Set<Integer> grade2 = new HashSet<>();
        grade2.add(R.drawable.grade2);

        Set<Integer> grade3 = new HashSet<>();
        grade3.add(R.drawable.grade3);

        Set<Integer> grade4 = new HashSet<>();
        grade4.add(R.drawable.grade4);

        Set<Integer> grade5 = new HashSet<>();
        grade5.add(R.drawable.grade5);

        surveyPictures[0] = grade0.iterator();
        surveyPictures[1] = grade1.iterator();
        surveyPictures[2] = grade2.iterator();
        surveyPictures[3] = grade3.iterator();
        surveyPictures[4] = grade4.iterator();
        surveyPictures[5] = grade5.iterator();

        return surveyPictures;
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
        Log.d(TAG, "got a database: db == null: " + (db == null));

        ContentValues values = new ContentValues();
        values.put(PictureEntry.COLUMN_NAME_PATH, picture.getFilePath());
        values.put(PictureEntry.COLUMN_NAME_SEVERITY, picture.getSeverity().getLevel());
        values.put(PictureEntry.COLUMN_NAME_PATIENT, 1); //TODO: very very bad, please change to actual patient id

        long newRowId = db.insert(PictureEntry.TABLE_NAME, null, values);

        db.close();
        
        Log.d(TAG, "inserted picture: " + newRowId);
        
        return newRowId != -1;
    }
    
    public boolean setPictureSeverity(String filename, AcneLevel sevLevel) {
        if(dbHelper == null)
            initializeDbHelper();
        
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        
        ContentValues cv = new ContentValues();
        cv.put(PictureEntry.COLUMN_NAME_SEVERITY, sevLevel.getName());
        
        int updated = db.update(PictureEntry.TABLE_NAME, cv, PictureEntry.COLUMN_NAME_PATH + " = " + filename, null);
        
        db.close();
        
        return updated > 0;
    }
}
