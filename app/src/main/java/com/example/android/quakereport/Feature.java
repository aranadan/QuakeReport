
package com.example.android.quakereport;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Feature {


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


}
