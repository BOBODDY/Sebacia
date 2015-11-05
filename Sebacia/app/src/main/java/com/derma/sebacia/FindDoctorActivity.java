package com.derma.sebacia;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class FindDoctorActivity extends ListActivity {
    public static String ACNE_LEVEL = "ACNE_LEVEL";
    private Location mLastLocation;
    private int acneLevel;
    private TextView lvlText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_doctor);

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

        List<String> messages = new ArrayList<>();
        if (mLastLocation != null) {
            String lastLocationString = "Latitute: " + String.valueOf(mLastLocation.getLatitude()) + "\nLongitude: " + String.valueOf(mLastLocation.getLongitude());
            messages.add(lastLocationString);
            for (int i = 0; i < 10; i++) {
                messages.add(lastLocationString);
            }
        } else {
            messages.add("Unable to determine location");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, messages);
        setListAdapter(adapter);
    }

    public void sendEmail(View view) {
        Intent sendIntent;

        sendIntent = new Intent(Intent.ACTION_SENDTO);
        sendIntent.setType("text/plain");
        sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"tpadaniel@aol.com"});
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Sebacia Patient Request");
        sendIntent.putExtra(Intent.EXTRA_TEXT, "testing sebacia email request");

        startActivity(Intent.createChooser(sendIntent, "Send Mail"));
    }

}
