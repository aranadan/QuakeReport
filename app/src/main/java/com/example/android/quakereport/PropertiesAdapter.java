package com.example.android.quakereport;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by andrey on 15.10.17.
 */

public class PropertiesAdapter extends RecyclerView.Adapter<PropertiesAdapter.ViewHolder> {
        private ArrayList<Feature> list;
    Context context;

    public PropertiesAdapter(ArrayList<Feature> list) {
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        //get View
        View v = LayoutInflater.from(context)
                .inflate(R.layout.activity_earthquake_list_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Properties properties = list.get(position).getProperties();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentCallWeb = new Intent(Intent.ACTION_VIEW, Uri.parse(properties.getUrl()));
                //call activity
                context.startActivity(intentCallWeb);
            }
        });

        holder.date.setText(EarthquakeActivity.dateFormat.format(new Date(properties.getTime())));
        holder.time.setText(new SimpleDateFormat("HH:mm").format(new Date(properties.getTime())));

        if (properties.getPlace().contains("of")) {
            String fullLocation = properties.getPlace();
            int indexDivision = fullLocation.indexOf("of") + 3;
            String region = fullLocation.substring(0, indexDivision);
            String offset = fullLocation.substring(indexDivision, fullLocation.length());
            //set TextView text
            holder.location.setText(region);
            holder.offset.setText(offset);

        } else {
            holder.offset.setText(properties.getPlace());
        }

        // Get the appropriate background color based on the current earthquake magnitude
        // Set the color on the magnitude circle
        holder.mag.setText(properties.getMag().toString());
        // Set the proper background color on the magnitude circle.
        // Fetch the background from the TextView, which is a GradientDrawable.
        GradientDrawable magnitudeCircle = (GradientDrawable) holder.mag.getBackground();
        magnitudeCircle.setColor(properties.getMagColor(context));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView mag, location, offset, date, time;

        public ViewHolder(View itemView) {
            super(itemView);
            mag = (TextView) itemView.findViewById(R.id.magnitude);
            location = (TextView) itemView.findViewById(R.id.location);
            offset = (TextView) itemView.findViewById(R.id.offset);
            date = (TextView) itemView.findViewById(R.id.date);
            time = (TextView) itemView.findViewById(R.id.time);
        }
    }

}
