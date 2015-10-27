package com.derma.sebacia;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.derma.sebacia.R;
import com.derma.sebacia.data.Patient;
import com.derma.sebacia.data.Picture;
import com.derma.sebacia.database.LocalDb;
import com.derma.sebacia.database.databaseInterface;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private static String TAG = "Sebacia";
    
    databaseInterface db;
    
//    ImageView imageView;
    ListView imageList;
    
    PictureAdapter adapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        //TODO: pull images from database

//        imageView = (ImageView)this.findViewById(R.id.hist_view_img);
        imageList = (ListView) findViewById(R.id.history_list);
//        Picture[] emptyList = {};
        ArrayList<Picture> emptyList = new ArrayList<>();
        adapter = new PictureAdapter(getApplicationContext(), R.id.textView, emptyList);
        imageList.setAdapter(adapter);
        
        new loadDbTask().execute(getApplicationContext());
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
//                Picture topPic = pictures.get(0);
                adapter.addAll(pictures);
//                Bitmap bmp = topPic.getPicBitmap();
//
//                imageView.setImageBitmap(bmp);
//                imageView.setOnClickListener(new ImageView.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Intent i = new Intent(getApplicationContext(), FindDoctorActivity.class);
//                        startActivity(i);
//                    }
//                });
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

    private class loadDbTask extends AsyncTask<Context, Void, Void> {

        protected Void doInBackground(Context... contexts) {

            db = new LocalDb(contexts[0]);

            return null;
        }

    }
    
    private class PictureAdapter extends ArrayAdapter<Picture> {
        
        public PictureAdapter(Context context, int textViewResourceId, ArrayList<Picture> pictures) {
            super(context, textViewResourceId, pictures);
            this.addAll(pictures);
        }
        
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if(v == null) {
                LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.activity_history, null);
            }
            
            Picture pic = this.getItem(position);
            if(pic != null) {
                ImageView iv = (ImageView) findViewById(R.id.history_list_item_image);
                if(iv != null) {
                    iv.setImageBitmap(pic.getPicBitmap());
                }
            }
            
            return v;
        }
    }
}
