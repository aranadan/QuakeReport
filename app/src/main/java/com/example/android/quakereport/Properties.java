
package com.example.android.quakereport;

import android.content.Context;
import android.support.v4.content.ContextCompat;

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


    public int getMagColor( Context context) {
        int output;
        //Возвращает наибольшее целое, меньшее или равное аргументу
        int magnitudeFloor = (int) Math.floor(mag);
        switch (magnitudeFloor){
            case 0:
            case 1:
                output = ContextCompat.getColor(context, R.color.magnitude1);
                break;
            case 2:
                output = ContextCompat.getColor(context, R.color.magnitude2);
                break;
            case 3:
                output = ContextCompat.getColor(context, R.color.magnitude3);
                break;
            case 4:
                output = ContextCompat.getColor(context, R.color.magnitude4);
                break;
            case 5:
                output = ContextCompat.getColor(context, R.color.magnitude5);
                break;
            case 6:
                output = ContextCompat.getColor(context, R.color.magnitude6);
                break;
            case 7:
                output = ContextCompat.getColor(context, R.color.magnitude7);
                break;
            case 8:
                output = ContextCompat.getColor(context, R.color.magnitude8);
                break;
            case 9:
                output = ContextCompat.getColor(context, R.color.magnitude9);
                break;
            default:
                output = ContextCompat.getColor(context, R.color.magnitude10plus);
                break;
        }
        return output;
    }

}
