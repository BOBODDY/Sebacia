package com.derma.sebacia;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.graphics.Typeface;
import android.widget.Button;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {
    Button btnProg, btnAbt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/ufonts.com_avantgarde-book.ttf");
        btnProg = (Button)findViewById(R.id.main_btn_prog);
        btnProg.setTypeface(face);

        btnAbt = (Button)findViewById(R.id.main_btn_abt);
        btnAbt.setTypeface(face);
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void startPageAbout(View view) {
        Intent intent = new Intent(this, PageAboutActivity.class);
        startActivity(intent);
    }
}
