package com.derma.sebacia.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
            return new Picture(filePath, new AcneLevel(sev, sev + ""), bytes);
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
                PictureEntry.COLUMN_NAME_PATH
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

                try {
                    FileInputStream fis = context.openFileInput(path);

                    int n = fis.available();
                    byte[] bytes = new byte[n];
                    int res = fis.read(bytes);

                    if (res != -1) {
                        pics.add(new Picture(path, null, bytes));
                    }

                } catch (FileNotFoundException fnfe) {
                    Log.e(TAG, "file not found", fnfe);
                } catch (IOException ioe) {
                    Log.e(TAG, "io exception", ioe);
                }

            } while (c.moveToNext());
        } else {
            Log.e(TAG, "no lines received from database");
        }
        
        c.close();
        
        return pics;
    }

    public List<Doctor> getDoctors(AcneLevel Severity){
        List<Doctor> doctors = new ArrayList<>();
        doctors.add(new Doctor("Cory Anderson", "2905 Premiere Pkwy, Duluth, GA 30097", 34, -84.5, 0, new AcneLevel(0, "Grade 0"), new AcneLevel(5, "Grade 5")));

        return doctors;
    }

    public Doctor getDoctor(int DocID){
        return null;
    }

    public Patient getPatient(Patient patient){
        return null;
    }

    public int[] getSurveyPictures() {
        int[] surveyPictures = new int[numQuestions];

        // TODO: Consider pulling these from a DB
        surveyPictures[0] = R.drawable.grade0;
        surveyPictures[1] = R.drawable.grade1;
        surveyPictures[2] = R.drawable.grade2;
        surveyPictures[3] = R.drawable.grade3;
        surveyPictures[4] = R.drawable.grade4;
        surveyPictures[5] = R.drawable.grade5;

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
        
        try {
            Log.d(TAG, "path to save picture: " + picture.getFilePath());
            FileOutputStream fos = context.openFileOutput(picture.getFilePath(), Context.MODE_PRIVATE);

            byte[] bytes = picture.getPic();
            if(bytes != null) {
                Log.d(TAG, "writing picture");
                fos.write(bytes);
            } else {
                Log.d(TAG, "no data to write");
            }
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
        values.put(PictureEntry.COLUMN_NAME_PATIENT, 1); //TODO: very very bad, please change to actual patient id

        long newRowId = db.insert(PictureEntry.TABLE_NAME, null, values);

        db.close();
        
        Log.d(TAG, "inserted picture: " + newRowId);
        
        return newRowId != -1;
    }
}
