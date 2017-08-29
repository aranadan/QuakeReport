package com.example.android.quakereport;


import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class Earthquake implements ClusterItem {
    private final String mLocation;
    private final Double mMagnitude;
    private final long mTime;
    private final String urlDetail;
    private String[] coordinates;

    public String getLocation() {
        return mLocation;
    }

    public Double getMagnitude() {
        return mMagnitude;
    }

    public long getTimeInMilliseconds() {
        return this.mTime;
    }

    public String getUrlDetail() {
        return urlDetail;
    }


    public Earthquake(Double magnitude, String location, long date, String url,String[] coordinates){
        this.mLocation = location;
        this.mMagnitude = magnitude;
        this.mTime = date;
        this.urlDetail = url;
        this.coordinates = coordinates;
    }

    @Override
    public LatLng getPosition() {
        return new LatLng(Double.parseDouble(coordinates[1]),Double.parseDouble(coordinates[0]));
    }

    @Override
    public String getTitle() {
        return mLocation;
    }

    @Override
    public String getSnippet() {
        return mMagnitude.toString();
    }
}
