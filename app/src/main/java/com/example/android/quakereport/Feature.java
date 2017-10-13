
package com.example.android.quakereport;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.maps.android.clustering.ClusterItem;

import java.text.SimpleDateFormat;

public class Feature implements ClusterItem{


    @SerializedName("properties")
    @Expose
    private Properties properties;
    @SerializedName("geometry")
    @Expose
    private Geometry geometry;


    public Properties getProperties() {
        return properties;
    }


    public Geometry getGeometry() {
        return geometry;
    }


    @Override
    public LatLng getPosition() {
        return new LatLng(geometry.getCoordinates().get(1),geometry.getCoordinates().get(0));
    }

    @Override
    public String getTitle() {
        return properties.getMag().toString();
    }

    @Override
    public String getSnippet() {
        return properties.getPlace() + " at " +
                new SimpleDateFormat("HH:mm").format(properties.getDate());
    }
}
