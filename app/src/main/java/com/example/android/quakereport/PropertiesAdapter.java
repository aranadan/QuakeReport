package com.example.android.quakereport;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;


public class PropertiesAdapter extends RecyclerView.Adapter<PropertiesAdapter.ViewHolder> {
        private List<Feature> list;
    private Context context;

    PropertiesAdapter(List<Feature> list) {
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        //get View
        View v = LayoutInflater.from(context)
                .inflate(R.layout.activity_earthquake_list_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Properties properties = list.get(position).getProperties();
        holder.itemView.setOnClickListener(view -> {
            Intent intentCallWeb = new Intent(Intent.ACTION_VIEW, Uri.parse(properties.getUrl()));
            //call activity
            context.startActivity(intentCallWeb);
        });

        holder.date.setText(DateFormat.format("EEE, MMM dd",properties.getTime()));
        holder.time.setText(DateFormat.format("HH:mm:ss",properties.getTime()));

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

        // Get the appropriate background color based on the current earthquake MAGNITUDE
        // Set the color on the MAGNITUDE circle
        holder.mag.setText(properties.getMag().toString());
        // Set the proper background color on the MAGNITUDE circle.
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
