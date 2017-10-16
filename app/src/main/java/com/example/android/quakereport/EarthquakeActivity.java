/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;



public class EarthquakeActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    public static int magnitude = 1;
    //URL to get JSON Array
    public static ArrayList<Feature> featureList;
    public static ArrayList<Feature> filteredListByMag;
    private SwipeRefreshLayout swipeRefreshLayout;
    public static SimpleDateFormat dateFormat;
    private SimpleDateFormat getDateFormatForQuery;
    private RecyclerView recyclerView;
    private Date date;
    private Quake quake;
    private FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);
        initialize();
        //download data and fill list
        retrofitRequest();
    }

    private void retrofitRequest() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://earthquake.usgs.gov/fdsnws/event/1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        QuakeService service = retrofit.create(QuakeService.class);
        Call<Quake> call = service.getQuery(magnitude, getDateFormatForQuery.format(date));
        Log.e("URL: ", call.request().url().toString());
        call.enqueue(new Callback<Quake>() {
            @Override
            public void onResponse(Call<Quake> call, Response<Quake> response) {

                if (response.isSuccessful()) {
                    featureList.clear();
                    quake = response.body();
                    featureList.addAll(quake.getFeatures());
                    filterListByMagnitude();
                }
            }

            @Override
            public void onFailure(Call<Quake> call, Throwable t) {
                Log.e("ERROR:", (t.getMessage()));
                Toast.makeText(EarthquakeActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRefresh() {
        // start show progress
        swipeRefreshLayout.setRefreshing(true);
        // wait 1 second and hide progress
        swipeRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
                //download data and fill list
                retrofitRequest();
            }

        }, 1000);
    }


    public void onClickChooseDate() {
        DatePickerFragment dialogFragment = new DatePickerFragment();
        dialogFragment.show(getFragmentManager(), "dialogFragment");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //save data to phone
        SharedPreferences sPref = getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = sPref.edit();
        //editor.putString("data", quake);
        editor.apply();
    }

    private class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        public DatePickerFragment() {}

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            date = new Date(year - 1900, monthOfYear, dayOfMonth);
            retrofitRequest();
        }


        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }
    }

    private void loadDataFromSD() {
        SharedPreferences sPref = getSharedPreferences("MyPref", MODE_PRIVATE);
        //jsonStr = sPref.getString("data", "");
    }

    //filter list by setting min magnitude
    private void filterListByMagnitude() {
        filteredListByMag.clear();
        for (Feature earthquake : featureList) {
            if (earthquake.getProperties().getMag() >= (double) magnitude) {
                filteredListByMag.add(earthquake);
            }
        }
        //set adapter to list
        //recyclerView.setAdapter(new EarthquakeAdapter(EarthquakeActivity.this, filteredListByMag));
        recyclerView.setAdapter(new PropertiesAdapter(filteredListByMag));
    }

    private void initialize() {
        featureList = new ArrayList<>();
        filteredListByMag = new ArrayList<>();

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeRefreshLayout.setOnRefreshListener(EarthquakeActivity.this);

        //set date
        date = new Date();
        dateFormat = new SimpleDateFormat("LLL dd, yyyy", Locale.UK);
        getDateFormatForQuery = new SimpleDateFormat("yyyy-MM-dd", Locale.UK);

        recyclerView = (RecyclerView) findViewById(R.id.list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new PropertiesAdapter(featureList));
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == RecyclerView.SCROLL_STATE_IDLE)
                {
                    fab.show();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0 || dy < 0 && fab.isShown())
                {
                    fab.hide();
                }
            }
        });


        /*recyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //create intent to call the browser
                Intent intentCallWeb = new Intent(Intent.ACTION_VIEW, Uri.parse(filteredListByMag.get(i).getProperties().getUrl()));
                //call activity
                startActivity(intentCallWeb);
            }
        });*/


        //floating action button to start map activity
        fab = (FloatingActionButton) findViewById(R.id.fab);
        //fab.attachToListView(recyclerView);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentMaps = new Intent(EarthquakeActivity.this, MapsActivity.class);
                startActivity(intentMaps);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_set_mag:

                Intent scaleIntent = new Intent(EarthquakeActivity.this, ScaleActivity.class);
                startActivityForResult(scaleIntent, 1);
                return true;

            case R.id.action_set_date:
                onClickChooseDate();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            filterListByMagnitude();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);//Menu Resource, Menu
        return true;
    }
}


