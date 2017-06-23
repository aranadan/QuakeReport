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
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
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


public class EarthquakeActivity extends AppCompatActivity {

    static TextView tvDate;
    static int selectedSpinnerItem = 1;
    //URL to get JSON Array
    private String URL;
    ListView listView;
    Spinner scaleSpinner;

    private ArrayList<Earthquake> earthquakeArrayList = new ArrayList<>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);
        scaleSpinner = (Spinner) findViewById(R.id.spinner);
        earthquakeArrayList = new ArrayList<>();
        tvDate = (TextView) findViewById(R.id.setDate);

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH)+1;
        int day = c.get(Calendar.DAY_OF_MONTH);
        final String date = year + "-" + month + "-" + day;
        tvDate.setText(date);
        setURLQuery(1, tvDate.getText().toString());


        //config adapter
        final ArrayAdapter<CharSequence> scaleAdapter = ArrayAdapter.createFromResource(this,R.array.scalelist,android.R.layout.simple_spinner_item);
        scaleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //call adapter
        scaleSpinner.setAdapter(scaleAdapter);

        scaleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (Integer.valueOf(selectedSpinnerItem) != Integer.valueOf(String.valueOf(scaleAdapter.getItem(position)))) {
                    selectedSpinnerItem = Integer.parseInt(scaleAdapter.getItem(position).toString());
                    //Toast.makeText(getApplicationContext(), selectedSpinnerItem, Toast.LENGTH_LONG).show();
                    setURLQuery(selectedSpinnerItem, tvDate.getText().toString());
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
                intentCallWeb.setData(Uri.parse( earthquakeArrayList.get(i).getUrlDetail()));
                //call activity
                startActivity(intentCallWeb);
            }
        });
        //listView.setOnScrollListener(this);

    }

     private class GetJson extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(EarthquakeActivity.this, "Json Data is downloading", Toast.LENGTH_LONG).show();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            HttpHandler  sh = new HttpHandler();

            // Making a request to URL and getting response
            String jsonStr = sh.makeServiceCall(URL);

            //Log.e(TAG, "Response from URL: " + jsonStr);
            if (jsonStr != null){
                try{
                    JSONObject jsonObject = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray featuresArray = jsonObject.getJSONArray("features");

                    //clear array if not null
                   if (earthquakeArrayList != null){
                        earthquakeArrayList.clear();
                    }

                    //looping through All features
                    for (int i = 0; i < featuresArray.length(); i++) {
                        JSONObject  object = featuresArray.getJSONObject(i);
                        JSONObject objectProperties = object.getJSONObject("properties");

                        Double mag = objectProperties.getDouble("mag");
                        String place = objectProperties.getString("place");
                        long time = objectProperties.getLong("time");
                        String urlDetail = objectProperties.getString("url");

                        //add data to arrayList
                        earthquakeArrayList.add(new Earthquake(mag,place,time,urlDetail));

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

           EarthquakeAdapter adapter = new EarthquakeAdapter(EarthquakeActivity.this, earthquakeArrayList);

            // Set the adapter on the {@link ListView}
            // so the list can be populated in the user interface
            // Find a reference to the {@link ListView} in the layout
            ListView earthquakeListView = (ListView) findViewById(R.id.list);
            earthquakeListView.setAdapter(adapter);
            //setURLQuery(selectedSpinnerItem,tvDate.getText().toString());
        }
    }

    public void onClickChooseDate(View v){
        DatePickerFragment dialogFragment = new DatePickerFragment();
        dialogFragment.show(getFragmentManager(),"dialogFragment");
    }

    public void setURLQuery(int magnitude,String date){
        URL = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&minmagnitude=" +magnitude+ "&starttime=" +date;//+ "&endtime=" + date;
        Log.e(HttpHandler.TAG,URL);
        new GetJson().execute();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(getApplicationContext(),"on pause",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(getApplicationContext(),"on resume",Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onStart() {
        super.onStart();
        Toast.makeText(getApplicationContext(),"on start",Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onStop() {
        super.onStop();
        Toast.makeText(getApplicationContext(),"on stop",Toast.LENGTH_SHORT).show();
    }

    private class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {


        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            int month = monthOfYear+1;
            String dateInString = year + "-" + month + "-" + dayOfMonth;
            tvDate.setText(dateInString);
            setURLQuery(selectedSpinnerItem,dateInString);
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


}


