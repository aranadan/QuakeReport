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

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class EarthquakeActivity extends AppCompatActivity {

    public static final String TAG = EarthquakeActivity.class.getName();

    //URL to get JSON Array
    private static final String URL = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2016-01-01&endtime=2016-01-31&minmag=6&limit=10";

    ArrayList<Earthquake> earthquakeArrayList = new ArrayList<>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        earthquakeArrayList = new ArrayList<>();
        new GetJson().execute();



    }

    private class GetJson extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(EarthquakeActivity.this,"Json Data is downloading",Toast.LENGTH_LONG).show();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            HttpHandler  sh = new HttpHandler();

            // Making a request to URL and getting response
            String url = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2016-01-01&endtime=2016-01-31&minmag=6&limit=10";
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from URL: " + jsonStr);
            if (jsonStr != null){
                try{
                    JSONObject jsonObject = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray featuresArray = jsonObject.getJSONArray("features");
                    //looping through All features
                    for (int i = 0; i < featuresArray.length(); i++) {
                        JSONObject  object = featuresArray.getJSONObject(i);
                        JSONObject objectProperties = object.getJSONObject("properties");

                        String mag = objectProperties.getString("mag");
                        String place = objectProperties.getString("place");
                        long time = objectProperties.getLong("time");

                        //add data to arrayList
                        earthquakeArrayList.add(new Earthquake(mag,place,time));

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }




            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
           // Toast.makeText(EarthquakeActivity.this,"Json Data is downloaded!!!",Toast.LENGTH_LONG).show();

           EarthquakeAdapter adapter = new EarthquakeAdapter(EarthquakeActivity.this, earthquakeArrayList);

            // Set the adapter on the {@link ListView}
            // so the list can be populated in the user interface
            // Find a reference to the {@link ListView} in the layout
            ListView earthquakeListView = (ListView) findViewById(R.id.list);
            earthquakeListView.setAdapter(adapter);

        }
    }


}


