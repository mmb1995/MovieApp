package com.example.android.popularmovies;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.popularmovies.Utils.MovieUtils;
import com.example.android.popularmovies.ViewModels.MovieViewModel;
import com.example.android.popularmovies.model.Movie;

import java.util.List;

// COMPLETED Refactor to remove AsyncTask and connect MainActivity with the ViewModel
public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String TAG = "Main_Activity";

    // Key for selected spinner value
    private static final String SPINNER_KEY = "spinner";

    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;
    private Bundle savedInstanceState;
    private Spinner spinner;
    private MovieViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if any data was passed into the activity from a previous state
        String searchTerm = MovieUtils.MOST_POPULAR;
        if (savedInstanceState != null) {
            this.savedInstanceState = savedInstanceState;
            searchTerm = savedInstanceState.getString(SPINNER_KEY);
        }

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Sets up the GridLayoutManager for the RecyclerView
        mRecyclerView = findViewById(R.id.rvMoviePosters);
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(MainActivity.this, 2);
        mRecyclerView.setLayoutManager(mGridLayoutManager);

        // Sets up the adapter for the RecyclerView
        Log.i(TAG,"Setting up RecyclerView");
        mMovieAdapter = new MovieAdapter(MainActivity.this);
        mRecyclerView.setAdapter(mMovieAdapter);

        // Set up ViewModel and Callback method
        mViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        mViewModel.init(searchTerm);
        mViewModel.getMovieData().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                // update the movie data held by the adapter
                Log.i(TAG, "Updating the adapter");
                mMovieAdapter.setMoviesList(movies);
            }
        });

        if (this.savedInstanceState == null && isConnected()) {
            // Begins an AsyncTask to query theMovieDB if there is no saved selection

        } else {
            // No network connection
            //Toast.makeText(this,"No Internet Connection Detected", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.appbar, menu);

        // Creates the spinner dropdown menu
        MenuItem item = menu.findItem(R.id.action_dropdown);
        this.spinner = (Spinner) item.getActionView();

        spinner.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.dropdown_array,
                android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        // Check for saved selection
        if (this.savedInstanceState != null) {
            spinner.setSelection(this.savedInstanceState.getInt(SPINNER_KEY, 0));
        } else {
            Toast.makeText(this,"No Internet Connection Detected", Toast.LENGTH_LONG).show();
        }

        return true;

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        String selectedItem = parent.getItemAtPosition(pos).toString();
        Log.i(TAG, selectedItem);
        String sortTerm = "";
        if (selectedItem.equals("Most Popular")) {
            sortTerm = MovieUtils.MOST_POPULAR;
        } else {
            sortTerm = MovieUtils.TOP_RATED;
        }

        if (isConnected()) {
            // Network is connected so preform background task
            mViewModel.refreshData(sortTerm);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Do nothing
        return;
    }


    /**
     * Checks wether the device is connected to a Network
     * @return True if the device is connected, false otherwise
     */
    public boolean isConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SPINNER_KEY, this.spinner.getSelectedItemPosition() );
    }
}
