package com.example.android.quakereport;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;



public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    private Date date;
    private OnDataPass mOnDataPasser;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mOnDataPasser = (OnDataPass) activity;
    }

    public DatePickerFragment() {
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        date = new Date(year - 1900, monthOfYear, dayOfMonth);
        mOnDataPasser.onDatePass(date);
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
