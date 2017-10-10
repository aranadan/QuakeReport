package com.example.android.quakereport;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;


public class EarthquakeIconRenderer extends DefaultClusterRenderer<Earthquake> {

    private Context context;
    private Paint p = new Paint();

    public EarthquakeIconRenderer(Context context, GoogleMap map, ClusterManager<Earthquake> clusterManager) {
        super(context, map, clusterManager);
        this.context = context;
    }

    @Override
    public Marker getMarker(Earthquake clusterItem) {
        return super.getMarker(clusterItem);
    }

    @Override
    protected void onBeforeClusterItemRendered(Earthquake item, MarkerOptions markerOptions) {

        int magnitudeFloor = (int) Math.floor(item.getMagnitude());

        switch (magnitudeFloor){
            case 1: p.setColor(ContextCompat.getColor(context, R.color.magnitude1));
                break;
            case 2: p.setColor(ContextCompat.getColor(context, R.color.magnitude2));
                break;
            case 3: p.setColor(ContextCompat.getColor(context, R.color.magnitude3));
                break;
            case 4: p.setColor(ContextCompat.getColor(context, R.color.magnitude4));
                break;
            case 5: p.setColor(ContextCompat.getColor(context, R.color.magnitude5));
                break;
            case 6: p.setColor(ContextCompat.getColor(context, R.color.magnitude6));
                break;
            case 7: p.setColor(ContextCompat.getColor(context, R.color.magnitude7));
                break;
            case 8: p.setColor(ContextCompat.getColor(context, R.color.magnitude8));
                break;
            case 9: p.setColor(ContextCompat.getColor(context, R.color.magnitude9));
                break;
        }

        // draw circle
        // diameter
        int d = 100;
        Bitmap bm = Bitmap.createBitmap(d, d, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bm);
        c.drawCircle(d/2, d/2, d/2, p);
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bm));
        markerOptions.alpha(0.8F);

        super.onBeforeClusterItemRendered(item, markerOptions);

    }

}
