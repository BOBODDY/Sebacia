package com.derma.sebacia;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.derma.sebacia.database.LocalDb;
import com.derma.sebacia.database.databaseInterface;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Set;


public class SurveyActivity extends Activity implements View.OnClickListener {
    ImageView imgCompare, imgSelfie;
    int compareIds[];
    Set<Integer> answers;
    int level;

    int currentQuestion;
    int prevQuestion;

    private final String TAG = "Sebacia";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);
        databaseInterface db = new LocalDb(getApplicationContext());

        compareIds = db.getSurveyPictures();

        currentQuestion = compareIds.length / 2;
        prevQuestion = currentQuestion;

        answers = new HashSet<>();

        imgCompare = (ImageView) findViewById(R.id.survey_img_compare);
        imgSelfie = (ImageView) findViewById(R.id.survey_img_selfie);

        // Filepath of selfie given as a bundle in intent
        Bundle b = getIntent().getExtras();
        Log.d(TAG, "in SurveyActivity onCreate()");
        if (b != null) {
            String filepath = b.getString("picturePath");
            Log.d(TAG, "looking at picture path: " + filepath);
            Bitmap bmp = null;
            try {
//                File f = new File(getFilesDir().getPath() + File.separator + filepath);
//                Log.d(TAG, "reading file from: " + f.getAbsolutePath());
                bmp = BitmapFactory.decodeFile(filepath);
            } catch (NullPointerException npe) {
                // Picture is null
                Log.e(TAG, "null pointer in survey", npe);
            }
            if (bmp != null) {
                Log.d(TAG, "bmp width: " + bmp.getWidth());
                Log.d(TAG, "bmp height: " + bmp.getHeight());
                imgSelfie.setImageBitmap(bmp);
            } else {
                Log.d(TAG, "bitmap is null");
            }
        } else {
            Log.d(TAG, "bundle is null");
        }

        // Set first image to compare with
        imgCompare.setImageResource(compareIds[currentQuestion]);

        // Set up the user interaction to manually show or hide the system UI.
        imgCompare.setOnClickListener(this);
        imgSelfie.setOnClickListener(this);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE
        );

        // Show Tutorial
        DialogFragment tutorialFrag = new SurveyTutorialFragment();
        tutorialFrag.show(getFragmentManager(), "tutorial_dialog");
    }


    @Override
    public void onClick(View v) {
        answers.add(currentQuestion);
        prevQuestion = currentQuestion;

        switch (v.getId()) {
            case R.id.survey_img_compare:
                currentQuestion++;
                break;
            case R.id.survey_img_selfie:
                currentQuestion--;
                break;
        }

        if (answers.contains(currentQuestion) || currentQuestion < 0 || currentQuestion >= compareIds.length) {
            level = prevQuestion;
            showResults();
        } else {
            // Continue the survey
            imgCompare.setImageResource(compareIds[currentQuestion]);
        }
    }

    private void showResults() {
        // Show the result
        DialogFragment resultsFrag = SurveyResultsFragment.newInstance(level);
        resultsFrag.show(getFragmentManager(), "results_dialog");
    }

    public void goToRecommendation(View view) {
        Intent intent = new Intent(view.getContext(), RecommendationActivity.class);
        intent.putExtra(RecommendationActivity.ACNE_LEVEL, level);
        startActivity(intent);
    }

}
