package com.example.manfredi.glicemy;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.manfredi.glicemy.db.model.Property;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Manfredi on 23/08/2016.
 */
public class ItemArrayAdapter extends ArrayAdapter<Property> {

    private Context context;
    private List<Property> glicemyProperty;

    //constructor
    public ItemArrayAdapter(Context context, int resource, ArrayList<Property> objects) {
        super(context, resource, objects);

        this.context = context;
        this.glicemyProperty = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {

            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.item, parent, false);

            holder = new ViewHolder();
            holder.timeTextView = (TextView) convertView.findViewById(R.id.timeTextViewItem);
            holder.dateTextView = (TextView) convertView.findViewById(R.id.dateTextViewItem);
            holder.valueTextView = (TextView) convertView.findViewById(R.id.valueTextViewItem);
            holder.mealTextView = (TextView) convertView.findViewById(R.id.mealTextView);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }


        Property property = glicemyProperty.get(position);


        holder.valueTextView.setText(property.getValue());
        holder.dateTextView.setText(property.getDate());
        holder.timeTextView.setText(property.getTime());
        holder.mealTextView.setText(property.getMeal());

        return convertView;
    }

    static class ViewHolder {
        private TextView valueTextView;
        private TextView dateTextView;
        private TextView timeTextView;
        private TextView mealTextView;
    }
}


