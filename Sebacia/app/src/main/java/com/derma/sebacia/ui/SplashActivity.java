package com.derma.sebacia.ui;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;

import com.derma.sebacia.R;
import com.derma.sebacia.database.DatabaseOpenHelper;
import com.derma.sebacia.database.LocalDb;


public class SplashActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Thread welcomeThread = new Thread() {

            @Override
            public void run() {
                try {
                    LocalDb db = new LocalDb(getApplicationContext());
                    super.run();
                    sleep(2000);  //Delay of 5 seconds
                } catch (Exception e) {
                    Log.e("Sebacia", "caught an exception in the splash screen", e);
                } finally {

                    Intent i = new Intent(SplashActivity.this,
                            MainActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        };
        welcomeThread.start();
    }
}
