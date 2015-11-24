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

                pics.add(new Picture(path, new AcneLevel(Integer.valueOf(sev), "IGA: " + sev)));

            } while (c.moveToNext());
        } else {
            Log.e(TAG, "no lines received from database");
        }
        
        c.close();
        
        return pics;
    }

    public List<Doctor> getDoctors(AcneLevel Severity){
        List<Doctor> doctors = new ArrayList<>();

        // TODO: Consider putting these in a Json file and parsing it or put in a DB
        doctors.add(new Doctor("Cory Anderson", "2905 Premiere Pkwy, Duluth, GA 30097", 34.005757, -84.093159, 0, 0, 5, "fakeEmial1@fake.fake"));
        doctors.add(new Doctor("Edmond Griffin", "5555 Peachtree Dunwoody Road, Atlanta, GA  30342", 33.873418, -84.3579319, 1, 0, 5, "fakeEmial2@fake.fake"));
        doctors.add(new Doctor("D. Scott Karempelis", "5555 Peachtree Dunwoody Road, Atlanta, GA  30342", 33.873418, -84.3579319, 2, 0, 5, "fakeEmial3@fake.fake"));
        doctors.add(new Doctor("Jason Smith", "103 John Maddox Drive NW, Rome, GA  30165-1419", 0, 0, 3, 0, 5, "fakeEmial4@fake.fake"));
        doctors.add(new Doctor("O. Talledo", "817 Aumond Place West, Augusta, GA  30909", 0, 0, 4, 0, 5, "fakeEmial5@fake.fake"));
        doctors.add(new Doctor("Joel Smith Jr.", "3271 Circle Oaks Drive NW, Atlanta, GA  30339", 0, 0, 5, 0, 5, "fakeEmial6@fake.fake"));
        doctors.add(new Doctor("James Bouchard", "865 Holcomb Bridge Road, Roswell, GA  30076-1954", 0, 0, 6, 0, 5, "fakeEmial7@fake.fake"));
        doctors.add(new Doctor("William Brown, Jr.", "3093 Oak Chase Drive NE, Roswell, GA  30075-5457", 0, 0, 7, 0, 5, "fakeEmial8@fake.fake"));
        doctors.add(new Doctor("Michael Sharkey", "2400 Bellevue Road, Dublin, GA  31021-2842", 0, 0, 8, 0, 5, "fakeEmial9@fake.fake"));
        doctors.add(new Doctor("Alexander Gross", "1505 Northside Boulevard, Cumming, GA  30041-7624", 0, 0, 9, 0, 5, "fakeEmial10@fake.fake"));
        doctors.add(new Doctor("Harold Brody", "1218 West Paces Ferry Road, Atlanta, GA  30327", 33.8493205, -84.4275171401218, 10, 0, 5, "fakeEmial11@fake.fake"));
        doctors.add(new Doctor("Michael Fisher", "5673 Peachtree Dunwoody Road, Atlanta, GA  30342", 33.873418, -84.3579319, 11, 0, 5, "fakeEmial12@fake.fake"));
        doctors.add(new Doctor("Gilbert Goldman", "5673 Peachtree Dunwoody Road NE, Atlanta, GA  30342-5023", 0, 0, 12, 0, 5, "fakeEmial13@fake.fake"));
        doctors.add(new Doctor("Abdul Hafeez", "631 Professional Dr,  Suite 110, Lawrenceville, GA 30046", 0, 0, 13, 0, 5, "fakeEmial14@fake.fake"));
        doctors.add(new Doctor("Jean Chapman", "3005 East Old Alabama Road, Alpharetta, GA  30022", 0, 0, 14, 0, 5, "fakeEmial15@fake.fake"));
        doctors.add(new Doctor("Bette Potter", "1004 Chafee Ave, Augusta, GA 30904-5810", 0, 0, 15, 0, 5, "fakeEmial16@fake.fake"));
        doctors.add(new Doctor("Jerry Cooper", "2171 Northlake Parkway LaVista O, Tucker, GA  30084", 0, 0, 16, 0, 5, "fakeEmial17@fake.fake"));
        doctors.add(new Doctor("Rutledge Forney", "59 East Park Lane NE, Atlanta, GA  30309-2725", 0, 0, 17, 0, 5, "fakeEmial18@fake.fake"));
        doctors.add(new Doctor("Windell Boutte", "4650 Stone Mountain Hwy, Lilburn, GA 30047", 0, 0, 18, 0, 5, "fakeEmial19@fake.fake"));
        doctors.add(new Doctor("Tiffani Hamilton", "11800 Atlantis Place, Alpharetta, GA  30022", 0, 0, 19, 0, 5, "fakeEmial20@fake.fake"));
        doctors.add(new Doctor("Michael Solomon", "2770 Lenox Road NE, Atlanta, GA  30324-6006", 0, 0, 20, 0, 5, "fakeEmial21@fake.fake"));
        doctors.add(new Doctor("James Sandwich", "1279 Highway 54 West, Fayetteville, GA  30214-4513", 0, 0, 21, 0, 5, "fakeEmial22@fake.fake"));
        doctors.add(new Doctor("Diane McGowan", "303 Smith Street, Lagrange, GA  30240-2745", 0, 0, 22, 0, 5, "fakeEmial23@fake.fake"));
        doctors.add(new Doctor("Sorahi Toloyan-Rahimi", "11700 Mercy Blvd, Savannah, GA 31419", 31.988311, -81.153477, 23, 0, 5, "fakeEmial24@fake.fake"));
        doctors.add(new Doctor("Joy Chastain", "1500 Oglethorpe Avenue, Athens, GA  30606", 33.9626984269836, -83.4266975137468, 24, 0, 5, "fakeEmial25@fake.fake"));
        doctors.add(new Doctor("Faith Jenkins", "1554 Riverside Drive, Gainesville, GA  30501", 34.3247936841869, -83.8306577737978, 25, 0, 5, "fakeEmial26@fake.fake"));
        doctors.add(new Doctor("Kendra Cole", "3655 Howell Ferry Road, Duluth, GA  30096", 34.0028350938707, -84.1623265235626, 26, 0, 5, "fakeEmial27@fake.fake"));
        doctors.add(new Doctor("Joshua Lane", "1210 Brookstone Centre Pkwy, Columbus, GA  31904-2954", 0, 0, 27, 0, 5, "fakeEmial28@fake.fake"));
        doctors.add(new Doctor("Sarah Ghayouri", "3115 Piedmont Rd NE, Atlanta, GA 30305-2529", 0, 0, 28, 0, 5, "fakeEmial29@fake.fake"));
        doctors.add(new Doctor("Darryl Hodson", "1025 Windfaire Pl, Roswell, GA  30076", 34.001649570751, -84.3118190922067, 29, 0, 5, "fakeEmial30@fake.fake"));
        doctors.add(new Doctor("Jay Kulkin", "1140 Hammond Drive, Atlanta, GA  30328", 0, 0, 30, 0, 5, "fakeEmial31@fake.fake"));
        doctors.add(new Doctor("Tara Abney", "5555 Peachtree Dunwody Road, Atlanta, GA  30342", 0, 0, 31, 0, 5, "fakeEmial32@fake.fake"));
        doctors.add(new Doctor("Dolores Rhymer-Anderson", "PO Box 1499, Tucker, GA 30085-1499", 0, 0, 32, 0, 5, "fakeEmial33@fake.fake"));
        doctors.add(new Doctor("Carmen Kavali", "5505 Peachtree Dunwoody Road, Atlanta, GA  30342", 33.873418, -84.3579319, 33, 0, 5, "fakeEmial34@fake.fake"));
        doctors.add(new Doctor("Rekha Singh", "2880 Lawrenceville-Suwanee Road, Suwanee, GA  30024", 34.0297482, -84.0481031, 34, 0, 5, "fakeEmial35@fake.fake"));
        doctors.add(new Doctor("Linda Kelley", "1905 Woodstock Rd, Roswell, GA 30075-5637", 0, 0, 35, 0, 5, "fakeEmial36@fake.fake"));
        doctors.add(new Doctor("Kathy Canada", "339 Holly Springs Road NE, White, GA  30184-2736", 0, 0, 36, 0, 5, "fakeEmial37@fake.fake"));
        doctors.add(new Doctor("Daniel Rabb", "974 South Enota Drive NE, Gainesville, GA  30501", 34.3178934055284, -83.8165735261293, 37, 0, 5, "fakeEmial38@fake.fake"));
        doctors.add(new Doctor("John Connors", "755 Mount Vernon Hwy NE, Atlanta, GA 30328-4274", 0, 0, 38, 0, 5, "fakeEmial39@fake.fake"));
        doctors.add(new Doctor("Wayne Emineth", "12502 Waterside Drive, Alpharetta, GA  30004", 0, 0, 39, 0, 5, "fakeEmial40@fake.fake"));
        doctors.add(new Doctor("Michael Busman", "PO Box 6815, Americus, GA  31709-6815", 0, 0, 40, 0, 5, "fakeEmial41@fake.fake"));
        doctors.add(new Doctor("Alan Gardner", "2550 Windy Hill Rd. Ste. 220, Marietta, GA 30067", 0, 0, 41, 0, 5, "fakeEmial42@fake.fake"));
        doctors.add(new Doctor("Thomas Gurley", "659 Auburn Avenue NE, Atlanta, GA  30312", 33.75574045, -84.366292308778, 42, 0, 5, "fakeEmial43@fake.fake"));
        doctors.add(new Doctor("Tanda Lane", "1210 Brookstone Centre Pkwy, Columbus, GA  31904-2954", 0, 0, 43, 0, 5, "fakeEmial44@fake.fake"));
        doctors.add(new Doctor("Avis Yount", "820 St Sebastian Way, Augusta, GA  30909", 0, 0, 44, 0, 5, "fakeEmial45@fake.fake"));
        doctors.add(new Doctor("David Van", "747 S Hill Street, Griffin, GA  30224", 33.2449382, -84.263914, 45, 0, 5, "fakeEmial46@fake.fake"));
        doctors.add(new Doctor("Michele Salazar", "2171 Northlake Parkway, Tucker, GA  30084", 33.8512075, -84.2469971, 46, 0, 5, "fakeEmial47@fake.fake"));
        doctors.add(new Doctor("Maureen Bosley", "3005 Old Alabama Road, Alpharetta, GA 30022", 0, 0, 47, 0, 5, "fakeEmial48@fake.fake"));
        doctors.add(new Doctor("Louis DeJoseph", "4553 N Shallowford Road, Atlanta, GA  30338", 0, 0, 48, 0, 5, "fakeEmial49@fake.fake"));
        doctors.add(new Doctor("Patricia Currin", "4300 Pleasant Hill Road, Duluth, GA  30096", 34.0047453470843, -84.1731130418395, 49, 0, 5, "fakeEmial50@fake.fake"));
        doctors.add(new Doctor("Joseph Payne", "5555 Peachtree-Dunwoody Road, Atlanta, GA  30342", 33.873418, -84.3579319, 50, 0, 5, "fakeEmial51@fake.fake"));
        doctors.add(new Doctor("David Whiteman", "3855 Pleasant Hill Road, Duluth, GA  30096", 33.9956904413333, -84.1625866397293, 51, 0, 5, "fakeEmial52@fake.fake"));
        doctors.add(new Doctor("Richard Ambrozic", "3321B North Valdosta Road, Valdosta, GA  31602", 30.8917893, -83.3181166, 52, 0, 5, "fakeEmial53@fake.fake"));
        doctors.add(new Doctor("Louis Cole", "3655 Howell Ferry Road, Duluth, GA  30096", 34.0028350938707, -84.1623265235626, 53, 0, 5, "fakeEmial54@fake.fake"));
        doctors.add(new Doctor("Kristina Price", "2165 Basque Dr SE, Smyrna, GA  30080-6508", 0, 0, 54, 0, 5, "fakeEmial55@fake.fake"));
        doctors.add(new Doctor("Marc Lozano", "1111 Drewsbury Ct SE, Smyrna, GA 30080", 33.8780066680507, -84.4879303876202, 55, 0, 5, "fakeEmial56@fake.fake"));
        doctors.add(new Doctor("Dana Martin", "707 Whitlock Avenue, H-9, Marietta, GA 30064", 0, 0, 56, 0, 5, "fakeEmial57@fake.fake"));
        doctors.add(new Doctor("Ronald Zieve", "6065 Roswell Rd NE, Atlanta, GA 30328-4013", 0, 0, 57, 0, 5, "fakeEmial58@fake.fake"));
        doctors.add(new Doctor("Christopher Killingsworth", "1100 Northside Drive, Suite 420, Cumming, GA 30041", 0, 0, 58, 0, 5, "fakeEmial59@fake.fake"));
        doctors.add(new Doctor("Kalen Wheeler", "107 W Paces Ferry Rd NW, Atlanta, GA 30305-1398", 0, 0, 59, 0, 5, "fakeEmial60@fake.fake"));
        doctors.add(new Doctor("Stephanie Norman", "4170 Oak Tree Ct, East Point, GA 30344-7021", 0, 0, 60, 0, 5, "fakeEmial61@fake.fake"));
        doctors.add(new Doctor("Anthony Lando", "2905 Premiere Pkwy, Duluth, GA 30097-5246", 0, 0, 61, 0, 5, "fakeEmial62@fake.fake"));
        doctors.add(new Doctor("Edlyn Jones", "1218 Fairburn Rd SW, Atlanta, GA 30331-2172", 0, 0, 62, 0, 5, "fakeEmial63@fake.fake"));

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
        grade1.add(R.drawable.iga1_00);
        grade1.add(R.drawable.iga1_01);

        Set<Integer> grade2 = new HashSet<>();
        grade2.add(R.drawable.grade2);
        grade2.add(R.drawable.iga2_00);
        grade2.add(R.drawable.iga2_01);
        grade2.add(R.drawable.iga2_02);

        Set<Integer> grade3 = new HashSet<>();
        grade3.add(R.drawable.grade3);
        grade3.add(R.drawable.iga3_00);
        grade3.add(R.drawable.iga3_01);
        grade3.add(R.drawable.iga3_02);
        grade3.add(R.drawable.iga3_03);
        grade3.add(R.drawable.iga3_04);
        grade3.add(R.drawable.iga3_05);
        grade3.add(R.drawable.iga3_06);
        grade3.add(R.drawable.iga3_07);
        grade3.add(R.drawable.iga3_08);
        grade3.add(R.drawable.iga3_09);
        grade3.add(R.drawable.iga3_10);
        grade3.add(R.drawable.iga3_11);
        grade3.add(R.drawable.iga3_12);
        grade3.add(R.drawable.iga3_13);
        grade3.add(R.drawable.iga3_14);
        grade3.add(R.drawable.iga3_15);
        grade3.add(R.drawable.iga3_16);
        grade3.add(R.drawable.iga3_17);
        grade3.add(R.drawable.iga3_18);
        grade3.add(R.drawable.iga3_19);
        grade3.add(R.drawable.iga3_20);
        grade3.add(R.drawable.iga3_21);

        Set<Integer> grade4 = new HashSet<>();
        grade4.add(R.drawable.grade4);
        grade4.add(R.drawable.iga4_00);
        grade4.add(R.drawable.iga4_01);
        grade4.add(R.drawable.iga4_02);
        grade4.add(R.drawable.iga4_03);

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
        cv.put(PictureEntry.COLUMN_NAME_SEVERITY, sevLevel.getLevel() + "");
        int updated = db.update(PictureEntry.TABLE_NAME, cv, PictureEntry.COLUMN_NAME_PATH + " = '" + filename + "'", null);
        
        db.close();
        
        return updated > 0;
    }
}
