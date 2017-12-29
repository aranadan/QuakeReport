package com.example.android.quakereport;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class EarthquakeActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, OnDataPass {

    private int magnitude;
    private List<Feature> featureList;
    public static List<Feature> FILTERED_LIST_BY_MAG;
    public static SimpleDateFormat DATE_FORMAT;

    private SwipeRefreshLayout swipeRefreshLayout;
    private SimpleDateFormat getDateFormatForQuery;
    private RecyclerView recyclerView;
    private Date date;
    private FloatingActionButton fab;
    private PropertiesAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);
        initialize();
        //download data and fill list
        retrofitRequest();
    }

    private void retrofitRequest() {
        featureList.clear();
        Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://earthquake.usgs.gov/fdsnws/event/1/")
                .build();

        QuakeService service = retrofit.create(QuakeService.class);

        Observable<Quake> mQuakeObservable = service.getQuery(1, getDateFormatForQuery.format(date));
        mQuakeObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(quake -> Observable.from(quake.getFeatures()))
                .subscribe(
                        //fill objects to list
                        q -> featureList.add(q)
                        //on error action
                        , throwable -> {
                        }
                        //on complete action
                        , this::filterListByMagnitude);
    }

    @Override
    public void onRefresh() {
        // start show progress
        swipeRefreshLayout.setRefreshing(true);
        // wait 1 second and hide progress
        swipeRefreshLayout.postDelayed(() -> {
            swipeRefreshLayout.setRefreshing(false);
            //download data and fill list
            retrofitRequest();
        }, 1000);
    }


    public void onClickChooseDate() {
        DatePickerFragment dialogFragment = new DatePickerFragment();
        dialogFragment.show(getFragmentManager(), "dialogFragment");
    }


    @Override
    public void onDataPass(int selectedScale) {
        magnitude = selectedScale;
        //filterListByMagnitude();
        retrofitRequest();
    }

    private class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        public DatePickerFragment() {
        }

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


    //filter list by setting min magnitude
    private void filterListByMagnitude() {
        FILTERED_LIST_BY_MAG.clear();
        /*FILTERED_LIST_BY_MAG
                .addAll(featureList.stream()
                .filter(feature -> feature.getProperties().getMag() >= magnitude)
                .collect(Collectors.toList()));*/
        for (Feature feature : featureList) {
            if (feature.getProperties().getMag() >= magnitude)
                FILTERED_LIST_BY_MAG.add(feature);
        }
        mAdapter.notifyDataSetChanged();
    }

    private void initialize() {
        magnitude = 1;
        featureList = new ArrayList<>();
        FILTERED_LIST_BY_MAG = new ArrayList<>();
        mAdapter = new PropertiesAdapter(FILTERED_LIST_BY_MAG);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeRefreshLayout.setOnRefreshListener(EarthquakeActivity.this);

        //set date
        date = new Date();
        DATE_FORMAT = new SimpleDateFormat("LLL dd, yyyy", Locale.UK);
        getDateFormatForQuery = new SimpleDateFormat("yyyy-MM-dd", Locale.UK);

        recyclerView = (RecyclerView) findViewById(R.id.list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    fab.show();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0 || dy < 0 && fab.isShown()) {
                    fab.hide();
                }
            }
        });


        //floating action button to start map activity
        fab = (FloatingActionButton) findViewById(R.id.fab);
        //fab.attachToListView(recyclerView);
        fab.setOnClickListener(view -> {
            Intent intentMaps = new Intent(EarthquakeActivity.this, MapsActivity.class);
            startActivity(intentMaps);
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_set_mag:

                FragmentManager manager = getFragmentManager();
                new ScaleFragment().show(manager, "ScaleFragment");
                return true;

            case R.id.action_set_date:
                onClickChooseDate();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);//Menu Resource, Menu
        return true;
    }

}


