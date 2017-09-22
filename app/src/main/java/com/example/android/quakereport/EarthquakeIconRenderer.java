package com.example.android.quakereport;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

/**
 * Created by admin on 22.09.2017.
 */

public class EarthquakeIconRenderer extends DefaultClusterRenderer<Earthquake> {

    public EarthquakeIconRenderer(Context context, GoogleMap map, ClusterManager<Earthquake> clusterManager) {
        super(context, map, clusterManager);
    }

    @Override
    protected void onBeforeClusterItemRendered(Earthquake item, MarkerOptions markerOptions) {
        int magnitudeFloor = (int) Math.floor(item.getMagnitude());

        switch (magnitudeFloor){
            case 1:  markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                markerOptions.alpha(1.0f);
                break;
            case 2:  markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                markerOptions.alpha(1.0f);
                break;
            case 3:  markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
                markerOptions.alpha(1.0f);
                break;
            case 4:  markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
                markerOptions.alpha(1.0f);
                break;
            case 5:  markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
                markerOptions.alpha(1.0f);
                break;
            case 6:  markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
                markerOptions.alpha(1.0f);
                break;
            case 7:  markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                markerOptions.alpha(1.0f);
                break;
        }
        //markerOptions.title(item.getTitle());

        super.onBeforeClusterItemRendered(item, markerOptions);

    }
}
