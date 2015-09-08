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

public class PageAboutActivity extends AppCompatActivity {
    TextView tvTitleCompany, tvContentCompany,
            tvTitleApp, tvContentApp;
    Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_about);

        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/ufonts.com_avantgarde-book.ttf");
        tvTitleCompany = (TextView)findViewById(R.id.pageabout_tv_title_company);
        tvTitleCompany.setTypeface(face);

        tvContentCompany = (TextView)findViewById(R.id.pageabout_tv_content_company);
        tvContentCompany.setTypeface(face);

        tvTitleApp = (TextView)findViewById(R.id.pageabout_tv_title_app);
        tvTitleApp.setTypeface(face);

        tvContentApp = (TextView)findViewById(R.id.pageabout_tv_content_app);
        tvContentApp.setTypeface(face);

        btnBack = (Button)findViewById(R.id.pageabout_btn_back);
        btnBack.setTypeface(face);
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

    public void startMain(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
