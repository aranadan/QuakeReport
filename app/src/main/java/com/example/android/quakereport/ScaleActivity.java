package com.example.android.quakereport;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.widget.NumberPicker;

public class ScaleActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scale);
        final Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        final NumberPicker numberPicker = (NumberPicker) findViewById(R.id.numberPicker);
        numberPicker.setMaxValue(10);
        numberPicker.setMinValue(1);
        //disable infinite scrolling
        numberPicker.setWrapSelectorWheel(false);
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {


                EarthquakeActivity.magnitude = newVal;
                //Vibrate for 30milliseconds
                vibrator.vibrate(30);
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
