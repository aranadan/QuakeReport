package com.example.android.quakereport;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.NumberPicker;

public class ScaleActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scale);
        //ListView listView = (ListView) findViewById(R.id.scale_list);
        //create scale array for earthquake magnitude
        /*final ArrayAdapter<Integer> scaleAdapter = new ArrayAdapter<Integer>(this.getApplicationContext(),
                android.R.layout.simple_list_item_1,new Integer[]{1,2,3,4,5,6,7,8,9,10});*/
        //listView.setAdapter(scaleAdapter);
        //listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

           /* @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intentResultItem = new Intent();
                intentResultItem.putExtra("scale",parent.getItemAtPosition(position).toString());
                setResult(RESULT_OK,intentResultItem);
                finish();
            }
        });*/
        final NumberPicker numberPicker = (NumberPicker) findViewById(R.id.numberPicker);
        numberPicker.setMaxValue(10);
        numberPicker.setMinValue(1);
        //disable infinite scrolling
        numberPicker.setWrapSelectorWheel(false);
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                EarthquakeActivity.magnitude = newVal;
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        Intent intentResultItem = new Intent();
        setResult(RESULT_OK, intentResultItem);
        finish();
    }
}
