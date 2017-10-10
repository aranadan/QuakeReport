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
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class EarthquakeActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    public static final String TAG = "myLogs";
    public static int magnitude = 1;
    //URL to get JSON Array
    public static String URL;
    private String jsonStr;
    public static ArrayList<Earthquake> earthquakeList;
    public static ArrayList<Earthquake> spinnerArrayList = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;
    public static SimpleDateFormat dateFormat;
    private SimpleDateFormat getDateFormatForQuery;
    private ListView listView;
    private Date date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);
        initialize();
        //download data and fill list
        new GetJson().execute(getDateFormatForQuery.format(date));
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
                new GetJson().execute(getDateFormatForQuery.format(date));
            }
        }, 1000);
    }

    private class GetJson extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(EarthquakeActivity.this, "Json Data is downloading", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Void doInBackground(String... date) {

            HttpHandler httpHandler = new HttpHandler();
            URL = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&minmagnitude="
                    + magnitude + "&starttime=" + date[0] + "T00:00:00" + "%2b03" + "&endtime=" + date[0] + "T23:59:59%2b03"; //%2b instead +
            // Making a request to URL and getting response
            //Log.e(TAG,URL.toString());
            jsonStr = httpHandler.makeServiceCall(URL);

            if (jsonStr != null) {
                getDataFromJson();
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                //get last saved data from sharedPreferences to variable jsonStr
                loadDataFromSD();
                //get data from local jsonStr
                getDataFromJson();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Load local json",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            filterListByMagnitude();
        }

        void getDataFromJson() {
            try {
                JSONObject jsonObject = new JSONObject(jsonStr);

                // Getting JSON Array node
                JSONArray featuresArray = jsonObject.getJSONArray("features");

                //clear array if not null
                if (earthquakeList != null) {
                    earthquakeList.clear();
                }

                //looping through All features
                for (int i = 0; i < featuresArray.length(); i++) {
                    JSONObject object = featuresArray.getJSONObject(i);
                    //get object for properties
                    JSONObject objectProperties = object.getJSONObject("properties");
                    //get object for coordinates
                    JSONObject objectGeometry = object.getJSONObject("geometry");
                    Double mag = objectProperties.getDouble("mag");
                    String place = objectProperties.getString("place");
                    long time = objectProperties.getLong("time");
                    String urlDetail = objectProperties.getString("url");
                    //get json array
                    JSONArray objectGeometryJSONArray = objectGeometry.getJSONArray("coordinates");
                    //convert json array to double array
                    String[] arrayCoordinates = objectGeometryJSONArray.join(",").split(",");
                    //add data to arrayList
                    earthquakeList.add(new Earthquake(mag, place, time, urlDetail, arrayCoordinates));

                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
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
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString("data", jsonStr);
        ed.apply();
    }

    class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        public DatePickerFragment() {}

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            date = new Date(year - 1900, monthOfYear, dayOfMonth);
            new GetJson().execute(getDateFormatForQuery.format(date));
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
        jsonStr = sPref.getString("data", "");
    }

    //filter list by setting min magnitude
    private void filterListByMagnitude() {
        spinnerArrayList.clear();
        for (Earthquake earthquake : earthquakeList) {
            if (earthquake.getMagnitude() >= (double) magnitude) {
                spinnerArrayList.add(earthquake);
            }
        }
        //set adapter to list
        listView.setAdapter(new EarthquakeAdapter(EarthquakeActivity.this, spinnerArrayList));
    }

    private void initialize() {

        earthquakeList = new ArrayList<>();
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeRefreshLayout.setOnRefreshListener(EarthquakeActivity.this);

        //set date
        date = new Date();
        dateFormat = new SimpleDateFormat("LLL dd, yyyy", Locale.UK);
        getDateFormatForQuery = new SimpleDateFormat("yyyy-MM-dd", Locale.UK);

        listView = (ListView) findViewById(R.id.list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //create intent to call the browser
                Intent intentCallWeb = new Intent(Intent.ACTION_VIEW, Uri.parse(spinnerArrayList.get(i).getUrlDetail()));
                //call activity
                startActivity(intentCallWeb);
            }
        });

        //floating action button to start map activity
        FloatingActionButton fabCallMaps = (FloatingActionButton) findViewById(R.id.fab);
        fabCallMaps.attachToListView(listView);
        fabCallMaps.setOnClickListener(new View.OnClickListener() {
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


