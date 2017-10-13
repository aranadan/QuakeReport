
package com.example.android.quakereport;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Properties {

    @SerializedName("mag")
    @Expose
    private Double mag;
    @SerializedName("place")
    @Expose
    private String place;
    @SerializedName("time")
    @Expose
    private Long time;
    @SerializedName("url")
    @Expose
    private String url;

    public String getUrl() {
        return url;
    }

    private int magColor;

    public int getMagColor() {
        return magColor;
    }

    public void setMagColor(int magColor) {
        this.magColor = magColor;
    }

    public Double getMag() {
        return mag;
    }

    public void setMag(Double mag) {
        this.mag = mag;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public Long getTime() {
        return time;
    }

    public Date getDate() {
        return new Date(time);
    }

    public void setTime(Long time) {
        this.time = time;
    }

}
