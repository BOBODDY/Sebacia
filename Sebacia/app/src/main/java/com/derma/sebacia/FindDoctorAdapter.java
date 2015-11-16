package com.derma.sebacia;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ActionMenuView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import java.util.List;

/**
 * Created by Daniel on 10/22/2015.
 */
public class FindDoctorAdapter extends ArrayAdapter {

    private final List<String> doctorList;
    private final Activity context;

    public FindDoctorAdapter(Activity context, List<String> list) {
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
//                Log.d("GroceryMate", "shopper requested");
//                Toast.makeText(context, "shopper requested", Toast.LENGTH_SHORT).show();
//                doctorList.remove(doctorList.get(position));
//                notifyDataSetChanged();
            }
        });
        textView.setText(doctorList.get(position));
        return view;
    }
}




