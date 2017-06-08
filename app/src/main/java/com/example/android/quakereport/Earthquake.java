package com.example.android.quakereport;


public class Earthquake  {
    private String mLocation;
    private String mMagnitude;
    private long mTime;

    public String getLocation() {
        return this.mLocation;
    }

    public String getMagnitude() {
        return this.mMagnitude;
    }

    public long getTimeInMilliseconds() {

        return mTime;
    }

    public Earthquake(String magnitude, String location, long date){
        mLocation = location;
        mMagnitude = magnitude;
        mTime = date;
    }
}
