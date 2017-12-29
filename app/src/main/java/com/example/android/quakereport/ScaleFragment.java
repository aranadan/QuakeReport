package com.example.android.quakereport;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;


public class ScaleFragment extends DialogFragment {

    private OnDataPass mDataPasser;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mDataPasser = (OnDataPass) activity;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.activity_scale, null);

        final Vibrator vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);

        final NumberPicker numberPicker = (NumberPicker) view.findViewById(R.id.numberPicker);
        numberPicker.setMaxValue(10);
        numberPicker.setMinValue(1);
        //disable infinite scrolling
        numberPicker.setWrapSelectorWheel(false);
        numberPicker.setOnValueChangedListener((numberPicker1, i, i1) -> vibrator.vibrate(20));

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle(R.string.set_scale)
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(android.R.string.ok, (dialogInterface, i) -> mDataPasser.onDataPass(numberPicker.getValue())).create();
    }
}
