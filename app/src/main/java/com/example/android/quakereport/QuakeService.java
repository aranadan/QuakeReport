package com.example.android.quakereport;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface QuakeService {


        @GET("query?format=geojson")
        Call<Quake> getQuery(@Query("minmagnitude") int minMag, @Query("starttime") String date);

}
