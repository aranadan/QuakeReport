package com.example.android.quakereport;


public class Earthquake  {
    private String mLocation;
    private Double mMagnitude;
    private long mTime;
    private String urlDetail;

    public String getLocation() {
        return this.mLocation;
    }

    public Double getMagnitude() {
        return this.mMagnitude;
    }

    public long getTimeInMilliseconds() {

        return mTime;
    }

    public String getUrlDetail() {
        return this.urlDetail;
    }

    public Earthquake(Double magnitude, String location, long date, String url){
        mLocation = location;
        mMagnitude = magnitude;
        mTime = date;
        urlDetail = url;
    }
}
