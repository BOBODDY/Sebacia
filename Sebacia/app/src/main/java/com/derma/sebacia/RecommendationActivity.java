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
    TextView txtRec, txtDet;
    Button btnFindDoc, btnProg;

    int surveyPictures[];
    int acneDescriptions[];
    int acneDetails[];
    int acneLevel = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendation);
        databaseInterface db = new LocalDb(getApplicationContext());

        surveyPictures = db.getSurveyPictures();
        acneDescriptions = getDescriptions();
        acneDetails = getDetails();

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            acneLevel = extras.getInt(ACNE_LEVEL);
        }

        imgHist = (ImageView)findViewById(R.id.recommendation_img_history);
        imgHist.setImageResource(surveyPictures[acneLevel]);

        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/ufonts.com_avantgarde-book.ttf");

        txtRec = (TextView)findViewById(R.id.recommendation_txt_rec);
        txtRec.setTypeface(face);
        txtRec.setText(acneDescriptions[acneLevel]);

        txtDet = (TextView)findViewById(R.id.recommendation_acne_short_detail);
        txtDet.setTypeface(face);
        txtDet.setText(acneDetails[acneLevel]);

        btnFindDoc = (Button)findViewById(R.id.recommendation_btn_finddoc);
        btnFindDoc.setTypeface(face);
        btnFindDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), FindDoctorActivity.class);
                intent.putExtra(FindDoctorActivity.ACNE_LEVEL, acneLevel);
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

    private int[] getDescriptions() {
        int[] descriptions = new int[6];
        descriptions[0] = R.string.description_acne_0;
        descriptions[1] = R.string.description_acne_1;
        descriptions[2] = R.string.description_acne_2;
        descriptions[3] = R.string.description_acne_3;
        descriptions[4] = R.string.description_acne_4;
        descriptions[5] = R.string.description_acne_5;

        return descriptions;
    }

    private int[] getDetails() {
        int[] details = new int[6];
        details[0] = R.string.detail_acne_0;
        details[1] = R.string.detail_acne_1;
        details[2] = R.string.detail_acne_2;
        details[3] = R.string.detail_acne_3;
        details[4] = R.string.detail_acne_4;
        details[5] = R.string.detail_acne_5;

        return details;
    }

}
