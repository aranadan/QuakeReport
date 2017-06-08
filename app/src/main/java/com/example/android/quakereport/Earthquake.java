package com.example.android.quakereport;


public class Earthquake  {
    private String mLocation;
    private Double mMagnitude;
    private long mTime;

    public String getLocation() {
        return this.mLocation;
    }

    public Double getMagnitude() {
        return this.mMagnitude;
    }

    public long getTimeInMilliseconds() {

        return mTime;
    }

    public Earthquake(Double magnitude, String location, long date){
        mLocation = location;
        mMagnitude = magnitude;
        mTime = date;
    }
}
