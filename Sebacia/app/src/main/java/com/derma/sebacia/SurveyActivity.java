package com.derma.sebacia;

import com.derma.sebacia.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;


public class SurveyActivity extends Activity {
    ImageView imgCompare, imgSelfie;
    int compareIds[];
    int[] answers;
    final int numQuestions = 6;  // TODO : make this a constant somewhere in the application
                                 //        because it corresponds to the number of acne classifications
                                 //        and other classes will make use of this value
    int currentQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);

        compareIds = new int[numQuestions];
        compareIds[0] = R.drawable.comp0;
        compareIds[1] = R.drawable.scissors0;
        compareIds[2] = R.drawable.dog0;
        compareIds[3] = R.drawable.penguin0;
        compareIds[4] = R.drawable.phone0;
        compareIds[5] = R.drawable.sun0;
        currentQuestion = 0;

        answers = new int[numQuestions];

        imgCompare = (ImageView)findViewById(R.id.survey_img_compare);
        imgSelfie = (ImageView)findViewById(R.id.survey_img_selfie);

        // Set up the user interaction to manually show or hide the system UI.
        imgCompare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleAcneClick(view);
            }
        });

        imgSelfie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleSelfieClick(view);
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

        DialogFragment tutorialFrag = new SurveyTutorialFragment();
        tutorialFrag.show(getFragmentManager(), "tutorial_dialog");
    }

    private void handleAcneClick (View view) {
        answers[currentQuestion++] = 1;
        handleSurveyResponse(view);
    }

    private void handleSelfieClick (View view) {
        answers[currentQuestion++] = 0;
        handleSurveyResponse(view);
    }

    private void handleSurveyResponse (View view) {
        if (currentQuestion == numQuestions) {
            DialogFragment resultsFrag = new SurveyResultsFragment ();
            resultsFrag.show(getFragmentManager(), "results_dialog");
        }
        else {
            imgCompare.setImageResource(compareIds[currentQuestion]);
        }
    }

    public void goToRecommendation (View view)
    {
        Intent intent = new Intent(view.getContext(), RecommendationActivity.class);
        startActivity(intent);
    }
}
