package com.derma.sebacia;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.graphics.Typeface;
import android.widget.Button;
import android.content.Intent;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {
    Button btnProg, btnAbt;
    ImageButton btnCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/ufonts.com_avantgarde-book.ttf");
        btnProg = (Button)findViewById(R.id.main_btn_prog);
        btnProg.setTypeface(face);
        btnProg.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), HistoryActivity.class);
                startActivity(i);
            }
        });

        btnAbt = (Button)findViewById(R.id.main_btn_abt);
        btnAbt.setTypeface(face);

        btnCamera = (ImageButton) findViewById(R.id.main_btn_cam);
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), CameraActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        Intent intent;
        switch(id) {
            case (R.id.action_settings) :
                intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            case (R.id.privacy) :
                intent = new Intent(this, PrivacyActivity.class);
                startActivity(intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void startPageAbout(View view) {
        Intent intent = new Intent(this, PageAboutActivity.class);
        startActivity(intent);
    }
}
