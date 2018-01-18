package com.example.android.quakereport;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

    private static int MAGNITUDE;
    private static String DIALOG_DATE = "dialogDate";
    private List<Feature> featureList;
    public static List<Feature> FILTERED_LIST_BY_MAG;
    public static SimpleDateFormat DATE_FORMAT;

    private SwipeRefreshLayout swipeRefreshLayout;
    private SimpleDateFormat getDateFormatForQuery;
    private RecyclerView recyclerView;
    private Date mDate;
    private FloatingActionButton fab;
    private PropertiesAdapter mAdapter;
    private DatePickerFragment datePickerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        if (savedInstanceState != null){
            mDate = (Date) savedInstanceState.getSerializable("date");
        }else
            mDate = new Date();

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

        Observable<Quake> mQuakeObservable = service.getQuery(1, getDateFormatForQuery.format(mDate));
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
        FragmentManager fragmentManager = getFragmentManager();
        datePickerFragment = new DatePickerFragment();
        datePickerFragment.show(fragmentManager, DIALOG_DATE);
    }


    //receive scale from ScaleFragment
    @Override
    public void onDataPass(int selectedScale) {
        MAGNITUDE = selectedScale;
        //filterListByMagnitude();
        retrofitRequest();
    }

    //receive date from DatePickerFragment
    @Override
    public void onDatePass(Date date) {
        mDate = date;
        retrofitRequest();
    }


    //filter list by setting min MAGNITUDE
    private void filterListByMagnitude() {
        FILTERED_LIST_BY_MAG.clear();
        /*FILTERED_LIST_BY_MAG
                .addAll(featureList.stream()
                .filter(feature -> feature.getProperties().getMag() >= MAGNITUDE)
                .collect(Collectors.toList()));*/
        for (Feature feature : featureList) {
            if (feature.getProperties().getMag() >= MAGNITUDE)
                FILTERED_LIST_BY_MAG.add(feature);
        }
        mAdapter.notifyDataSetChanged();
    }

    private void initialize() {
        featureList = new ArrayList<>();
        FILTERED_LIST_BY_MAG = new ArrayList<>();
        mAdapter = new PropertiesAdapter(FILTERED_LIST_BY_MAG);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeRefreshLayout.setOnRefreshListener(EarthquakeActivity.this);

        //set mDate

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
        FragmentManager manager = getFragmentManager();
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_set_mag:
                new ScaleFragment().show(manager, "ScaleFragment");
                return true;
            case R.id.action_set_date:
                new DatePickerFragment().show(manager, DIALOG_DATE);
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mDate != null){
            outState.putSerializable("date", mDate);
        }

    }
}


