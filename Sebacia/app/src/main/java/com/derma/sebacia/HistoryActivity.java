package com.derma.sebacia;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.derma.sebacia.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class HistoryActivity extends AppCompatActivity {

    private static String TAG = "HistoryActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        ImageView imageView = (ImageView)this.findViewById(R.id.hist_view_img);

        try {

            Bitmap bm = BitmapFactory.decodeStream(openFileInput("20150915_123503.jpg"));
            imageView.setImageBitmap(bm);
            imageView.setOnClickListener(new ImageView.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getApplicationContext(), FindDoctorActivity.class);
                    startActivity(i);
                }
            });
        } catch(FileNotFoundException fnfe) {
            Log.e(TAG, "file not found", fnfe);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_history, menu);
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
