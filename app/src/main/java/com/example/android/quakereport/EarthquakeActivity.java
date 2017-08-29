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
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;


public class EarthquakeActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "myLogs" ;
    private TextView tvDate;
    private int magnitude = 1;
    //URL to get JSON Array
    private String URL;
    private ListView listView;
    private Spinner scaleSpinner;
    private String jsonStr;
    public static ArrayList<Earthquake> earthquakeList;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FloatingActionButton fabCallMaps;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);
        initialize();
        //download data and fill list
        setURLQuery(tvDate.getText().toString());
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
                setURLQuery(tvDate.getText().toString());
            }
        },1000);
    }

    private class GetJson extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(EarthquakeActivity.this, "Json Data is downloading", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            HttpHandler  sh = new HttpHandler();

            // Making a request to URL and getting response
            jsonStr = sh.makeServiceCall(URL);

            if (jsonStr == null)
                loadDataFromSD();

            if (jsonStr != null ){

                try{
                    JSONObject jsonObject = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray featuresArray = jsonObject.getJSONArray("features");


                    //clear array if not null
                   if (earthquakeList != null){
                        earthquakeList.clear();
                    }

                    //looping through All features
                    for (int i = 0; i < featuresArray.length(); i++) {
                        JSONObject  object = featuresArray.getJSONObject(i);
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
                        String[] arrayCoordinates =  objectGeometryJSONArray.join(",").split(",");

                        //Log.e(TAG, objectGeometryJSONArray.toString());
                        //add data to arrayList
                        earthquakeList.add(new Earthquake(mag,place,time,urlDetail,arrayCoordinates));

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else {
                //Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_SHORT).show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
           super.onPostExecute(result);
           filterListByMagnitude(magnitude);
        }
    }

    public void onClickChooseDate(View v){
        DatePickerFragment dialogFragment = new DatePickerFragment();
        dialogFragment.show(getFragmentManager(),"dialogFragment");
    }

    public void setURLQuery(String date){
        earthquakeList.clear();
        URL = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&minmagnitude="
                + 1 + "&starttime=" + date + "T00:00:00"+"%2b03" + "&endtime=" + date + "T23:59:59%2b03"; //%2b вместо +
        Log.e(HttpHandler.TAG,URL);
        new GetJson().execute();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Toast.makeText(getApplicationContext(),"on pause",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
    @Override
    protected void onStart() {
        super.onStart();
    }
    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //save data from server to phone
        SharedPreferences sPref = getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString("data", jsonStr);
        ed.apply();
        Toast.makeText(this, "Text saved", Toast.LENGTH_SHORT).show();

    }

    private class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            int month = monthOfYear+1;
            String dateInString = year +"-" + month +"-"+ dayOfMonth;
            tvDate.setText(dateInString);
            setURLQuery(dateInString);
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(),this,year,month,day);
        }
    }

    private void loadDataFromSD() {

        SharedPreferences sPref = getSharedPreferences("MyPref", MODE_PRIVATE);
        jsonStr = sPref.getString("data", "");
        Log.e(HttpHandler.TAG,jsonStr);
    }

    //filter list by setting min magnitude
    private void filterListByMagnitude(int minMagnitude){
        ArrayList<Earthquake> earthquakeArrayList = new ArrayList<>();
        for (Earthquake earthquake : this.earthquakeList){
            if (earthquake.getMagnitude()>= (double) minMagnitude){
                earthquakeArrayList.add(earthquake);
            }
        }
        //create new adapter
        EarthquakeAdapter adapter = new EarthquakeAdapter(EarthquakeActivity.this,earthquakeArrayList);
        //create listView
        ListView earthquakeListView = (ListView) findViewById(R.id.list);
        //set adapter to list
        earthquakeListView.setAdapter(adapter);
        Toast.makeText(this, "Magnitude is " + minMagnitude, Toast.LENGTH_SHORT).show();

    }
    private void initialize(){

        scaleSpinner = (Spinner) findViewById(R.id.spinner);
        earthquakeList = new ArrayList<>();
        tvDate = (TextView) findViewById(R.id.setDate);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeRefreshLayout.setOnRefreshListener(EarthquakeActivity.this);

        //floating action button to start map activity
        fabCallMaps = (FloatingActionButton) findViewById(R.id.fab);
        fabCallMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent intentMaps = new Intent(EarthquakeActivity.this,MapsActivity.class);
                startActivity(intentMaps);
            }
        });

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH)+1;
        int day = c.get(Calendar.DAY_OF_MONTH);
        final String date = year + "-" + month + "-" + day;
        tvDate.setText(date);

        //config adapter
        final ArrayAdapter<CharSequence> scaleAdapter = ArrayAdapter.createFromResource(this,R.array.scalelist,android.R.layout.simple_spinner_item);
        scaleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //call adapter
        scaleSpinner.setAdapter(scaleAdapter);
        scaleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (Integer.valueOf(magnitude) != Integer.valueOf(String.valueOf(scaleAdapter.getItem(position)))) {
                    magnitude = Integer.parseInt(scaleAdapter.getItem(position).toString());
                    filterListByMagnitude(magnitude);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        listView = (ListView) findViewById(R.id.list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //create intent to call the browser
                Intent intentCallWeb = new Intent(Intent.ACTION_VIEW);
                //put data to intent
                intentCallWeb.setData(Uri.parse( earthquakeList.get(i).getUrlDetail()));
                //call activity
                startActivity(intentCallWeb);
            }
        });
    }
}


