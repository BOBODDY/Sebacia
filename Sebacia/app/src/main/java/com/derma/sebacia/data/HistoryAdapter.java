package com.derma.sebacia.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.derma.sebacia.R;

import java.util.List;

/**
 * Created by nick on 11/3/15.
 * 
 * Adapter for the history page that holds the image and severity level
 */
public class HistoryAdapter extends ArrayAdapter<Picture> {
    
    private final Context context;
    private final List<Picture> pictures;
    
    public HistoryAdapter(Context context, List<Picture> pictures) {
        super(context, R.layout.history_list_item);
        
        this.context = context;
        this.pictures = pictures;
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.history_list_item, parent, false);
        TextView acneLevel = (TextView) rowView.findViewById(R.id.history_list_item_level);
        ImageView selfie = (ImageView) rowView.findViewById(R.id.history_list_item_image);
        
        Picture pic = pictures.get(position);
        if(pic.getSeverity() != null) {
            acneLevel.setText(pic.getSeverity().getName());
        } else {
            acneLevel.setText("N/A");
        }
        selfie.setImageBitmap(pic.getPicThumbnail());
        
        return rowView;
    }
    
    public void addAll(List<Picture> pictures) {
        this.pictures.addAll(pictures);
        notifyDataSetChanged();
    }
    
    @Override
    public int getCount() {
        return pictures.size();
    }
}
