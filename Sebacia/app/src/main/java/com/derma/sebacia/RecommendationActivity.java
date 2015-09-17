package com.derma.sebacia;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class RecommendationActivity extends Activity {
    ImageView imgHist;
    TextView txtRec;
    Button btnNext, btnPrev, btnFindDoc, btnCam;

    int testImgIds[];
    final int numTestImgs = 7;
    int currentImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendation);

        testImgIds = new int[numTestImgs];
        testImgIds[0] = R.drawable.key0;
        testImgIds[1] = R.drawable.comp0;
        testImgIds[2] = R.drawable.scissors0;
        testImgIds[3] = R.drawable.dog0;
        testImgIds[4] = R.drawable.penguin0;
        testImgIds[5] = R.drawable.phone0;
        testImgIds[6] = R.drawable.sun0;
        currentImg = 0;

        imgHist = (ImageView)findViewById(R.id.recommendation_img_history);

        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/ufonts.com_avantgarde-book.ttf");

        txtRec = (TextView)findViewById(R.id.recommendation_txt_rec);
        txtRec.setTypeface(face);

        btnNext = (Button)findViewById(R.id.recommendation_btn_next);
        btnNext.setTypeface(face);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextImage(v);
            }
        });

        btnPrev = (Button)findViewById(R.id.recommendation_btn_prev);
        btnPrev.setTypeface(face);
        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previoiusImage(v);
            }
        });

        btnFindDoc = (Button)findViewById(R.id.recommendation_btn_finddoc);
        btnFindDoc.setTypeface(face);
        btnFindDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), FindDoctorActivity.class);
                startActivity(intent);
            }
        });

        btnCam = (Button)findViewById(R.id.recommendation_btn_cam);
        btnCam.setTypeface(face);
        btnCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), CameraActivity.class);
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

    private void nextImage (View view)
    {
        currentImg = (currentImg + 1) % numTestImgs;
        handleImageView(view);
    }

    private void previoiusImage (View view)
    {
        currentImg = (currentImg - 1) % numTestImgs;
        handleImageView(view);
    }

    private void handleImageView (View view)
    {
        imgHist.setImageResource(testImgIds[currentImg]);
    }
}
