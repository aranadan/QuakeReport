package com.example.android.quakereport;


import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Earthquake implements ClusterItem {
    private final String mLocation;
    private final Double mMagnitude;
    private final long mTime;
    private final String urlDetail;
    private String[] coordinates;
    private int magColor;

    public int getMagColor() {
        return magColor;
    }

    public void setMagColor(int magColor) {
        this.magColor = magColor;
    }

    public String getLocation() {
        return mLocation;
    }

    public Double getMagnitude() {
        return mMagnitude;
    }

    public Date getDate() {
        return new Date(mTime);
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

    public Earthquake() {
        mLocation = null;
        urlDetail = null;
        mMagnitude = null;
        mTime = 0;

    }

    @Override
    public String getTitle() {

        return mMagnitude.toString();
    }

    @Override
    public String getSnippet() {
        return  getLocation() + " at " + new SimpleDateFormat("HH:mm").format(getDate());
    }
}
