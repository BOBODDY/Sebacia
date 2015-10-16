package com.derma.sebacia;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.derma.sebacia.database.LocalDb;
import com.derma.sebacia.database.databaseInterface;


public class RecommendationActivity extends Activity {
    // used to put extra from survey activity
    public static final String ACNE_LEVEL = "LEVEL";

    ImageView imgHist;
    TextView txtRec;
    Button btnFindDoc, btnProg;

    int surveyPictures[];
    int acneLevel = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendation);
        databaseInterface db = new LocalDb(getApplicationContext());

        surveyPictures = db.getSurveyPictures();

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            acneLevel = extras.getInt(ACNE_LEVEL);
        }

        imgHist = (ImageView)findViewById(R.id.recommendation_img_history);
        imgHist.setImageResource(surveyPictures[acneLevel]);

        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/ufonts.com_avantgarde-book.ttf");

        txtRec = (TextView)findViewById(R.id.recommendation_txt_rec);
        txtRec.setTypeface(face);

        btnFindDoc = (Button)findViewById(R.id.recommendation_btn_finddoc);
        btnFindDoc.setTypeface(face);
        btnFindDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), FindDoctorActivity.class);
                startActivity(intent);
            }
        });

        btnProg = (Button)findViewById(R.id.recommendation_btn_prog);
        btnProg.setTypeface(face);
        btnProg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), HistoryActivity.class);
                startActivity(intent);
            }
        });

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE
        );
    }

}
