package com.example.android.quakereport;


public class Earthquake  {
    private final String mLocation;
    private final Double mMagnitude;
    private final long mTime;
    private final String urlDetail;

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

    public Earthquake(Double magnitude, String location, long date, String url){
        this.mLocation = location;
        this.mMagnitude = magnitude;
        this.mTime = date;
        this.urlDetail = url;
    }
}
