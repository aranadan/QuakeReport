package com.example.android.quakereport;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.example.android.quakereport.R.color;
import com.example.android.quakereport.R.id;
import com.example.android.quakereport.R.layout;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class EarthquakeAdapter extends ArrayAdapter<Earthquake> {

    public EarthquakeAdapter(Context context, ArrayList<Earthquake> earthquakes) {
        super(context, 0, earthquakes);
    }


    @Override
    public View getView(int position,  View convertView,  ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null){
            listItemView = LayoutInflater.from(this.getContext()).inflate(layout.activity_earthquake_list_item, parent, false);
        }
        //Find earthquake at the given position in the list
        Earthquake currentEarthquake = this.getItem(position);

        //Find the TextView with view ID magnitude
        TextView magnitudeView = (TextView) listItemView.findViewById(id.magnitude);
        magnitudeView.setText(this.formatDecimal(currentEarthquake.getMagnitude()));

        //Find the TextView with view ID location
        TextView locationView = (TextView) listItemView.findViewById(id.location);

        //Find the TextView with view ID offset
        TextView offsetView = (TextView) listItemView.findViewById(id.offset);

        //Split location on two TextView fields
        String fullLocation = currentEarthquake.getLocation();
        if (fullLocation.contains("of")){
            int indexDivision = fullLocation.indexOf("of") + 3;
            String region =fullLocation.substring(0,indexDivision);
            String offset = fullLocation.substring(indexDivision, fullLocation.length());

            //set TextView text
            locationView.setText(region);
            offsetView.setText(offset);
        }

        // Create a new Date object from the time in milliseconds of the earthquake
        Date dateObject = new Date(currentEarthquake.getTimeInMilliseconds());

        //Find the TextView with view ID date
        TextView dateView = (TextView) listItemView.findViewById(id.date);
        dateView.setText(this.formatDate(dateObject));

        TextView timeView = (TextView) listItemView.findViewById(id.time);
        timeView.setText(this.formatTime(dateObject));

        // Set the proper background color on the magnitude circle.
        // Fetch the background from the TextView, which is a GradientDrawable.
        GradientDrawable magnitudeCircle = (GradientDrawable) magnitudeView.getBackground();

        // Get the appropriate background color based on the current earthquake magnitude
        int magnitudeColor = this.getMagnitudeColor(currentEarthquake.getMagnitude());

        // Set the color on the magnitude circle
        magnitudeCircle.setColor(magnitudeColor);

        return listItemView;
    }

    private int getMagnitudeColor(double magnitude) {
        int output;
        //Возвращает наибольшее целое, меньшее или равное аргументу
        int magnitudeFloor = (int) Math.floor(magnitude);
        switch (magnitudeFloor){
            case 0:
            case 1:
                output = ContextCompat.getColor(this.getContext(), color.magnitude1);
                break;
            case 2:
                output = ContextCompat.getColor(this.getContext(), color.magnitude2);
                break;
            case 3:
                output = ContextCompat.getColor(this.getContext(), color.magnitude3);
                break;
            case 4:
                output = ContextCompat.getColor(this.getContext(), color.magnitude4);
                break;
            case 5:
                output = ContextCompat.getColor(this.getContext(), color.magnitude5);
                break;
            case 6:
                output = ContextCompat.getColor(this.getContext(), color.magnitude6);
                break;
            case 7:
                output = ContextCompat.getColor(this.getContext(), color.magnitude7);
                break;
            case 8:
                output = ContextCompat.getColor(this.getContext(), color.magnitude8);
                break;
            case 9:
                output = ContextCompat.getColor(this.getContext(), color.magnitude9);
                break;
            default:
                output = ContextCompat.getColor(this.getContext(), color.magnitude10plus);
                break;
        }
        return output;
    }

    /**
     * Return the formatted date string (i.e. "Mar 3, 1984") from a Date object.
     */
    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObject);
    }

    /**
     * Return the formatted date string (i.e. "4:30 PM") from a Date object.
     */
    private String formatTime(Date dateObject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);
    }

    private String formatDecimal (Double magnitude){
        DecimalFormat decimalFormat = new DecimalFormat("0.0");
        String output = decimalFormat.format(magnitude);
        return output;
    }
}
