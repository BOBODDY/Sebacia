package com.derma.sebacia.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;

import com.derma.sebacia.R;


public class SplashActivity extends Activity {

    /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

//        SharedPreferences settings = getSharedPreferences("shared_preferences", 0);
//
//        if (settings.getBoolean("isAccepted", false)) {
//            //the app is being launched for first time, do something
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//
//            builder.setMessage(R.string.privacy_statement)
//                    .setTitle("Privacy Statement");
//
//            builder.setPositiveButton(R.string.privacy_agree, new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int id) {
//                    // User clicked OK button
//                    SharedPreferences settings = getSharedPreferences("shared_preferences", 0);
//                    settings.edit().putBoolean("isAccepted", true).commit();
//                    Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
//                    startActivity(mainActivity);
//                }
//            });
//
//            builder.setNegativeButton(R.string.privacy_disagree, new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int id) {
//                    // User cancelled the dialog
//                    finish();
//                }
//            });
//
//            AlertDialog dialog = builder.create();
//            dialog.show();

            // first time task

            // record the fact that the app has been started at least once


        Thread welcomeThread = new Thread() {

            @Override
            public void run() {
                try {
                    super.run();
                    sleep(5000);  //Delay of 5 seconds
                } catch (Exception e) {

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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splash, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
