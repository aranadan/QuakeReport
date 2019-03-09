package com.example.android.quakereport;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.util.Log;

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
        /*Paint p = new Paint();
        p.setColor(item.getProperties().getMagColor(context));

        // draw circle
        // diameter
        int d = 100;
        Bitmap bm = Bitmap.createBitmap(d, d, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bm);
        c.drawCircle(d/2, d/2, d/2, p);*/

        /*Drawable d = context.getDrawable(R.drawable.magnitude_circle_map);
        Bitmap bm = BitmapFactory.decodeResource(context.getResources(),R.drawable.magnitude_circle_map);*/

        //markerOptions.icon(BitmapDescriptorFactory.fromBitmap(Bitmap.createBitmap(bm)));

        int colorId = item.getProperties().getMagColor(context);
        Log.d("Test color", colorId + "");
        //markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
        markerOptions.icon(BitmapDescriptorFactory.fromResource(context.getDrawable(R.drawable.magnitude_circle_map)));
        markerOptions.alpha(0.8F);
        super.onBeforeClusterItemRendered(item, markerOptions);
    }

}
