package com.example.android.quakereport;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;


public class EarthquakeIconRenderer extends DefaultClusterRenderer<Feature> {

    private Context context;

    public EarthquakeIconRenderer(Context context, GoogleMap map, ClusterManager<Feature> clusterManager) {
        super(context, map, clusterManager);
        this.context = context;
    }

    @Override
    public Marker getMarker(Feature clusterItem) {
        return super.getMarker(clusterItem);
    }

    @Override
    protected void onBeforeClusterItemRendered(Feature item, MarkerOptions markerOptions) {
        //создаю кастомный значек для отображения на карте
       /* Paint p = new Paint();
        p.setColor(item.getProperties().getMagColor(context));

        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.magnitude_circle_map);
        // draw circle
        // diameter
        int d = 100;
        Bitmap bm = Bitmap.createBitmap(d, d, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bm);
        canvas.drawCircle(d/2, d/2, d/2, p);
        drawable.draw(canvas);
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bm));*/
       int mag = item.getProperties().getMag().intValue();

        switch (mag){
            case (1): markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            break;
            case (2): markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
            break;
            case (3): markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
            break;
            case (4): markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
            break;
            case (5): markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
            break;
            case (6): markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            break;
            case (7): markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            break;
            case (8): markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
            break;
            case (9): markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
            break;
            case (10): markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
            break;

        }

        //markerOptions.alpha(0.8F);
        super.onBeforeClusterItemRendered(item, markerOptions);
    }

}
