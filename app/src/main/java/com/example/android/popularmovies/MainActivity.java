package com.example.android.popularmovies;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
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

import com.example.android.popularmovies.Utils.MovieUtils;
import com.example.android.popularmovies.ViewModels.MovieViewModel;
import com.example.android.popularmovies.model.Movie;

import java.util.List;

// COMPLETED Refactor to remove AsyncTask and connect MainActivity with the ViewModel
public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String TAG = "Main_Activity";

    // Key for selected spinner value
    private static final String SPINNER_POSITION = "spinnerPos";
    private static final String SPINNER_VALUE = "spinnerVal";

    private MovieAdapter mMovieAdapter;
    private Bundle savedInstanceState;
    private Spinner spinner;
    private MovieViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if any data was passed into the activity from a previous state
        String searchTerm;
        if (savedInstanceState != null) {
            Log.i(TAG, "Restoring previous state");
            this.savedInstanceState = savedInstanceState;
            searchTerm = savedInstanceState.getString(SPINNER_VALUE, MovieUtils.MOST_POPULAR);
        } else {
            searchTerm = MovieUtils.MOST_POPULAR;
        }

        setContentView(R.layout.activity_main);
        Log.i(TAG, searchTerm);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Sets up the GridLayoutManager for the RecyclerView
        RecyclerView mRecyclerView = findViewById(R.id.rvMoviePosters);
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(MainActivity.this, 2);
        mRecyclerView.setLayoutManager(mGridLayoutManager);

        // Sets up the adapter for the RecyclerView
        Log.i(TAG, "Setting up RecyclerView");
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.appbar, menu);

        // Creates the spinner dropdown menu
        MenuItem item = menu.findItem(R.id.action_dropdown);
        this.spinner = (Spinner) item.getActionView();

        spinner.setOnItemSelectedListener(this);

        // Create the spinner's adapter
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.dropdown_array, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        // Check for saved selection
        if (this.savedInstanceState != null) {
            spinner.setSelection(this.savedInstanceState.getInt(SPINNER_POSITION, 0));
        }

        return true;

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        String selectedItem = parent.getItemAtPosition(pos).toString();
        Log.i(TAG, selectedItem);

        String sortTerm;
        if (selectedItem.equals("Most Popular")) {
            sortTerm = MovieUtils.MOST_POPULAR;
        } else {
            sortTerm = MovieUtils.TOP_RATED;
        }

        // refresh the data in the ViewModel
        mViewModel.refreshData(sortTerm);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Do nothing
        return;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(TAG,"Saving spinner selection");
        outState.putInt(SPINNER_POSITION, this.spinner.getSelectedItemPosition());
        outState.putString(SPINNER_VALUE, spinner.getSelectedItem().toString());
    }
}
