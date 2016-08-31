package com.example.manfredi.glicemy;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
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
    private int color;

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
        String meal = property.getMeal();

        holder.valueTextView.setText(property.getValue());
        holder.dateTextView.setText(property.getDate());
        holder.timeTextView.setText(property.getTime());
        holder.mealTextView.setText(meal);

        int value = Integer.parseInt(property.getValue());


        if (value < 70) {
            color = Color.parseColor("#fcbe2a");
        }
        else if ( (value > 180 && meal.equals("Dopo")) || (value > 140 && meal.equals("Prima")) ) {
            color = Color.parseColor("#e30613");
        }
        else if ( (value > 69 && value < 180 && meal.equals("Dopo")) || (value > 69 && value < 141 && meal.equals("Prima")) ) {
            color = Color.parseColor("#009640");
        }
        else {
            color = Color.BLACK;
        }

        holder.valueTextView.setTextColor(color);

        return convertView;
    }

    static class ViewHolder {
        private TextView valueTextView;
        private TextView dateTextView;
        private TextView timeTextView;
        private TextView mealTextView;
    }
}


