package com.derma.sebacia.ui;

import android.app.Activity;
import android.app.DialogFragment;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.derma.sebacia.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SurveyTutorialFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SurveyTutorialFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SurveyTutorialFragment extends DialogFragment {

    private OnFragmentInteractionListener mListener;

    Button btnOK;
    TextView txtTutorial;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SurveyTutorialFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SurveyTutorialFragment newInstance(String param1, String param2) {
        SurveyTutorialFragment fragment = new SurveyTutorialFragment();
        return fragment;
    }

    public SurveyTutorialFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_survey_tutorial, container, false);

        Typeface face = Typeface.createFromAsset(getActivity().getAssets(), "fonts/ufonts.com_avantgarde-book.ttf");

        txtTutorial = (TextView)v.findViewById(R.id.survey_turorial_txt_txt);
        txtTutorial.setTypeface(face);

        btnOK = (Button)v.findViewById(R.id.survey_turorial_btn_ok);
        btnOK.setTypeface(face);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
