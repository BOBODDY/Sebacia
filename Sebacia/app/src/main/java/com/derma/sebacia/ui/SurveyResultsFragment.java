package com.derma.sebacia.ui;

import android.app.DialogFragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.derma.sebacia.R;


public class SurveyResultsFragment extends DialogFragment {
    // parameters
    private static final String ACNE_LEVEL = "level";
    private String acneLevel;

    // view items
    TextView txtExpl, txtLvlTxt, txtLvlNum;
    Button btnRec;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param level Parameter 1.
     * @return A new instance of fragment SurveyResultsFragment.
     */
    public static SurveyResultsFragment newInstance(int level) {
        SurveyResultsFragment fragment = new SurveyResultsFragment();
        Bundle args = new Bundle();
        args.putString(ACNE_LEVEL, Integer.toString(level));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Get the acne level from the survey
        try {
            acneLevel = getArguments().getString(ACNE_LEVEL);
        } catch (NullPointerException e) {
            // TODO: Exit the app
        }

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_survey_results, container, false);

        Typeface face = Typeface.createFromAsset(getActivity().getAssets(), "fonts/ufonts.com_avantgarde-book.ttf");

        txtExpl = (TextView)v.findViewById(R.id.survey_results_txt_expl);
        txtExpl.setTypeface(face);

        txtLvlTxt = (TextView)v.findViewById(R.id.survey_results_txt_lvltxt);
        txtLvlTxt.setTypeface(face);

        txtLvlNum = (TextView)v.findViewById(R.id.survey_results_txt_lvlnum);
        txtLvlNum.setText(acneLevel);
        txtLvlNum.setTypeface(face);

        btnRec = (Button)v.findViewById(R.id.survey_results_btn_rec);
        btnRec.setTypeface(face);
        btnRec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SurveyActivity)getActivity()).goToRecommendation(v);
            }
        });

        return v;
    }

}
