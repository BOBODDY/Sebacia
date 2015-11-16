package com.derma.sebacia;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import com.derma.sebacia.data.Doctor;

import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.derma.sebacia.data.AcneLevel;
import com.derma.sebacia.database.LocalDb;
import com.derma.sebacia.database.databaseInterface;

import java.util.ArrayList;
import java.util.List;

public class FindDoctorActivity extends ListActivity {
    public static String ACNE_LEVEL = "ACNE_LEVEL";
    private Location mLastLocation;
    private int acneLevel;
    private TextView lvlText;
    private double minDistForNearest = 100000000; // returned haversine distance in km
    private int maxDoctorsDisplayed = 10;
    
    private final String TAG = "Sebacia";

    // Used for haversine
    static final double Radius = 6372.8; // Radius of the Earth in km

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_doctor);
        databaseInterface db = new LocalDb(getApplicationContext());

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            acneLevel = extras.getInt(ACNE_LEVEL);
        }

        lvlText = (TextView) findViewById(R.id.finddoc_level_txt);
        lvlText.setText(Integer.toString(acneLevel));

        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                mLastLocation = location;
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {
                Toast.makeText(FindDoctorActivity.this, "Location is " + mLastLocation, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if (getApplicationContext().checkCallingOrSelfPermission("android.permission.ACCESS_FINE_LOCATION") == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            mLastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }

        // Used for debugging
        //Toast.makeText(FindDoctorActivity.this, "Location is " + mLastLocation, Toast.LENGTH_LONG).show();


        List<Doctor> doctors = db.getDoctors(new AcneLevel(acneLevel, ""));
        List<String> messages = new ArrayList<>();
        if (mLastLocation != null) {
            int numDoctorsDisplayed = 0;
            for (Doctor doctor : doctors) {
                if(haversine(mLastLocation.getLatitude(), mLastLocation.getLongitude(), doctor.getLat(), doctor.getLong()) < minDistForNearest && numDoctorsDisplayed < maxDoctorsDisplayed) {
                    Log.v(TAG, "adding " + doctor.getAdress());
                    messages.add(doctor.getAdress());
                    numDoctorsDisplayed++;
                }
            }
        } else {
            messages.add("Unable to determine location");
        }
        ArrayAdapter<Doctor> adapter = new FindDoctorAdapter(this, doctors);
        setListAdapter(adapter);
    }

    private double haversine(double lat1, double long1, double lat2, double long2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(long2 - long1);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        double a = Math.pow(Math.sin(dLat / 2),2) + Math.pow(Math.sin(dLon / 2),2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return Radius * c;
    }
}
