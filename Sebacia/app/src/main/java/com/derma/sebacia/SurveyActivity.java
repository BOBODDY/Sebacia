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

import com.derma.sebacia.data.AcneLevel;
import com.derma.sebacia.data.Picture;
import com.derma.sebacia.database.LocalDb;
import com.derma.sebacia.database.databaseInterface;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


public class SurveyActivity extends Activity implements View.OnClickListener {
    // Survey layout variables
    ImageView imgCompare, imgSelfie;

    // Survey results handling
    Set<Integer> answers;   // Keep track of decisions the user made per survey
    int level;              // Final acne level
    int timesTaken = 0;     // Keep track of how many times the user has taken the survey
    int minTimesTaken = 2;  // The minimum number of times the user has to take the survey
    int[][] results;        // Results from each survey to determine final level

    // Survey navigation
    Iterator<Integer> compareIds[];
    int currentQuestion;
    int prevQuestion;

    // DB
//    databaseInterface db = new LocalDb(getApplicationContext());
    databaseInterface db;
    String filepath = null;

    private final String TAG = "Sebacia";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);
        db = new LocalDb(getApplicationContext());

        // Get the pictures
        compareIds = db.getSurveyPictures();

        // Prepare survey navigation
        currentQuestion = compareIds.length / 2;
        prevQuestion = currentQuestion;

        // Prepare survey results
        answers = new HashSet<>();                  // Keep track of answers of current survey
        results = new int[compareIds.length][1];    // Keep track of results between surveys

        // Initialize results to keep track of how frequent a result occurs
        // TODO: Use something other than a double array (vector)
        for(int i=0; i<results.length; i++) {
            results[i] = new int[1];
            results[i][0] = 0;
        }

        // Set initial pictures
        imgCompare = (ImageView) findViewById(R.id.survey_img_compare);
        imgSelfie = (ImageView) findViewById(R.id.survey_img_selfie);

        // Filepath of selfie given as a bundle in intent
        Bundle b = getIntent().getExtras();
        Log.d(TAG, "in SurveyActivity onCreate()");
        if (b != null) {
            filepath = b.getString("picturePath");
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
        imgCompare.setImageResource(compareIds[currentQuestion].next());

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

        // Navigate the survey
        switch (v.getId()) {
            case R.id.survey_img_compare:
                currentQuestion++;
                break;
            case R.id.survey_img_selfie:
                currentQuestion--;
                break;
        }

        if (answers.contains(currentQuestion) || currentQuestion < 0 || currentQuestion >= compareIds.length) {
            timesTaken++;
            if (currentQuestion < 0) {
                results[0][0]++;
            } else if (currentQuestion > compareIds.length - 1) {
                results[compareIds.length - 1][0]++;
            } else {
                results[currentQuestion][0]++;
            }

            // Reset the survey
            answers = new HashSet<>();
            currentQuestion = compareIds.length / 2;

            // Check if we still have images to show
            boolean stillHaveIDs = true;
            for(Iterator<Integer> id : compareIds) {
                if(!id.hasNext()) {
                    stillHaveIDs = false;
                }
            }

            if(timesTaken >= minTimesTaken || !stillHaveIDs) {
                // TODO: Make this more efficient besides using a double array (vector)
                int max = -1;
                for(int i=0; i<results.length; i++) {
                    int[] result = results[i];
                    if(result[0] > max) {
                        max = result[0];
                        level = i;
                    }
                }

                // Save the result and go to the details
                saveResults();
                showResults();
            } else {
                // Continue the survey
                imgCompare.setImageResource(compareIds[currentQuestion].next());
            }
        } else {
            // Continue the survey
            imgCompare.setImageResource(compareIds[currentQuestion].next());
        }
    }

    private void saveResults() {
        // Only save results when a picture was taken
        if(filepath != null) {
            db.setPictureSeverity(filepath, new AcneLevel(level, "IGA: " + level));
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
