package com.derma.sebacia.ui;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.derma.sebacia.R;
import com.derma.sebacia.data.HistoryAdapter;
import com.derma.sebacia.data.Patient;
import com.derma.sebacia.data.Picture;
import com.derma.sebacia.database.LocalDb;
import com.derma.sebacia.database.databaseInterface;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private static String TAG = "Sebacia";
    
    databaseInterface db;

    ListView imageList;
    
    HistoryAdapter adapter;

    List<Picture> pics;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        imageList = (ListView) findViewById(R.id.history_list);

        new loadDbTask().execute(getApplicationContext());
    }
    
    private class getPicturesTask extends AsyncTask<Void, Void, List<Picture>> {
        public List<Picture> doInBackground(Void... params) {
            pics = db.getPatientPics(new Patient(1));
            
            return pics;
        }
        
        
        protected void onPostExecute(List<Picture> pictures) {
            if(pictures.size() > 0) {
                adapter = new HistoryAdapter(getApplicationContext(), pictures);
                imageList.setAdapter(adapter);
                imageList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent i = new Intent(getApplicationContext(), FindDoctorActivity.class);
                        i.putExtra("ACNE_LEVEL", pics.get(position).getSeverity().getLevel());
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

    private class loadDbTask extends AsyncTask<Context, Void, Void> {

        protected Void doInBackground(Context... contexts) {

            db = new LocalDb(contexts[0]);

            return null;
        }

        protected void onPostExecute(Void aVoid) {
            new getPicturesTask().execute();
        }
    }
}
