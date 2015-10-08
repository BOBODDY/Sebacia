package com.derma.sebacia;

import android.app.ListActivity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class FindDoctorActivity extends ListActivity {
    private Location mLastLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_doctor);


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

        if(getApplicationContext().checkCallingOrSelfPermission("android.permission.ACCESS_FINE_LOCATION") == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            mLastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }

        // Used for debugging
        //Toast.makeText(FindDoctorActivity.this, "Location is " + mLastLocation, Toast.LENGTH_LONG).show();

        List<String> messages = new ArrayList<>();
        if(mLastLocation != null) {
            String lastLocationString = "Latitute: " + String.valueOf(mLastLocation.getLatitude()) + "\nLongitude: " + String.valueOf(mLastLocation.getLongitude());
            messages.add(lastLocationString);
            for(int i=0; i < 10; i++) {
                messages.add(lastLocationString);
            }
        } else {
            messages.add("Unable to determine location");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, messages);
        setListAdapter(adapter);
    }

}
