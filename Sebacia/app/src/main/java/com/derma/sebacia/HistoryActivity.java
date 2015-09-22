package com.derma.sebacia;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.derma.sebacia.R;
import com.derma.sebacia.data.Patient;
import com.derma.sebacia.data.Picture;
import com.derma.sebacia.database.LocalDb;
import com.derma.sebacia.database.databaseInterface;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private static String TAG = "Sebacia";
    
    databaseInterface db;
    
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        //TODO: pull images from database

        imageView = (ImageView)this.findViewById(R.id.hist_view_img);
        
        db = new LocalDb(getApplicationContext());
    }
    
    protected void onStart() {
        super.onStart();
        
        new getPicturesTask().execute();
    }
    
    private class getPicturesTask extends AsyncTask<Void, Void, List<Picture>> {
        public List<Picture> doInBackground(Void... params) {
            List<Picture> pics = db.getPatientPics(new Patient(1));
            
            return pics;
        }
        
        
        protected void onPostExecute(List<Picture> pictures) {
            if(pictures.size() > 0) {
                Picture topPic = pictures.get(0);
                Bitmap bmp = topPic.getPicBitmap();

                imageView.setImageBitmap(bmp);
                imageView.setOnClickListener(new ImageView.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(getApplicationContext(), FindDoctorActivity.class);
                        startActivity(i);
                    }
                });
            }
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
