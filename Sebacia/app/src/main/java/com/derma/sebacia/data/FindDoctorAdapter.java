package com.derma.sebacia.data;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;


import com.derma.sebacia.R;
import com.derma.sebacia.data.Doctor;
import com.derma.sebacia.ui.SurveyQuestionsActivity;

import java.util.List;

/**
 * Created by Daniel on 10/22/2015.
 */
public class FindDoctorAdapter extends ArrayAdapter {

    private final List<Doctor> doctorList;
    private final Activity context;

    public FindDoctorAdapter(Activity context, List<Doctor> list) {
        super(context, R.layout.row_layout_doclist, list);
        this.context = context;
        doctorList = list;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(view == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            view = inflater.inflate(R.layout.row_layout_doclist, null);
        }
        Button requestButton = (Button) view.findViewById(R.id.buttonMakeRequest);
        TextView textView = (TextView) view.findViewById(R.id.textViewShopperListRow);
        requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    String emailFromList = doctorList.get(position).getEmail();
                    Intent i = new Intent(getContext(), SurveyQuestionsActivity.class);
                    i.putExtra("email", emailFromList);
                    getContext().startActivity(i);
            }
        });
        textView.setText(doctorList.get(position).toString());
        return view;
    }
}




