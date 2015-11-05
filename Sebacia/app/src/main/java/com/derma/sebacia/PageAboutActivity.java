package com.derma.sebacia;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import android.graphics.Typeface;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import android.content.Intent;

import com.derma.sebacia.data.Doctor;
import com.derma.sebacia.database.DoctorDB;

import java.util.List;

public class PageAboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_about);

//        TextView title = (TextView) findViewById(R.id.pageabout_tv_title_app);
//        String mock = "GO AWAY";
//        try {
//            DoctorDB docDB = new DoctorDB();
//            docDB.execute(DoctorDB.request.COORDINATE.ordinal(), 0,3, 3, 2);
//            List<Doctor> print = docDB.get();
//            mock = print.toString();
//            title.setText(mock);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_page_about, menu);
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
