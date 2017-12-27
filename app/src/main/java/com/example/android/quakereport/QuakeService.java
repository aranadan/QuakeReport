package com.example.android.quakereport;



import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;


public interface QuakeService {


        @GET("query?format=geojson")
        Observable<Quake> getQuery(@Query("minmagnitude") int minMag, @Query("starttime") String date);

}
