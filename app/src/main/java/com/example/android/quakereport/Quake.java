
package com.example.android.quakereport;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Quake {


    @SerializedName("features")
    @Expose
    private List<Feature> features = null;


    public List<Feature> getFeatures() {
        return features;
    }

    public void setFeatures(List<Feature> features) {
        this.features = features;
    }

}
