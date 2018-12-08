package com.example.android.popularmovies;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
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
import com.example.android.popularmovies.adapter.MovieAdapter;
import com.example.android.popularmovies.adapter.RecyclerViewClickListener;
import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.remote.MovieApiResource;

// COMPLETED Refactor to remove AsyncTask and connect MainActivity with the ViewModel

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener,
        RecyclerViewClickListener, SwipeRefreshLayout.OnRefreshListener{

    private static final String TAG = "Main_Activity";

    // Key for selected mSpinner value
    private static final String BUNDLE_POSITION = "spinnerPos";
    private static final String BUNDLE_VALUE = "spinnerVal";

    private MovieAdapter mMovieAdapter;
    private Spinner mSpinner;
    private Bundle mBundle;
    private MovieViewModel mViewModel;

    // Insures spinner's onItemSelected method is not called when app is first created
    Boolean isLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if any data was passed into the activity from a previous state
        String searchTerm;
        if (savedInstanceState != null) {
            Log.i(TAG, "Restoring previous state");
            this.mBundle = savedInstanceState;
            searchTerm = savedInstanceState.getString(BUNDLE_VALUE, MovieUtils.MOST_POPULAR);
        } else {
            searchTerm = MovieUtils.MOST_POPULAR;
        }

        // Begin setting up ui
        setContentView(R.layout.activity_main);
        Log.i(TAG, searchTerm);
        isLoading = true;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Sets up the GridLayoutManager for the RecyclerView
        RecyclerView mRecyclerView = findViewById(R.id.rvMoviePosters);

        // Set the onRefreshListener to handle user swipe events
        SwipeRefreshLayout mSwipeRefresh = findViewById(R.id.refresh);
        mSwipeRefresh.setOnRefreshListener(this);

        // Checks the orientation to decide how to display the grid
        int orientation = getResources().getConfiguration().orientation;
        StaggeredGridLayoutManager mGridLayoutManager;
        if(orientation == Configuration.ORIENTATION_PORTRAIT) {
            mGridLayoutManager = new StaggeredGridLayoutManager(2,
                    StaggeredGridLayoutManager.VERTICAL);
        } else {
            mGridLayoutManager = new StaggeredGridLayoutManager(3,
                    StaggeredGridLayoutManager.VERTICAL);
        }

        // Set the layout manager
        mRecyclerView.setLayoutManager(mGridLayoutManager);

        // Sets up the adapter for the RecyclerView
        mMovieAdapter = new MovieAdapter(MainActivity.this, MainActivity.this);
        mRecyclerView.setAdapter(mMovieAdapter);

        // Set up ViewModel and Callback method
        mViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        mViewModel.init(searchTerm);
        mViewModel.getMovieData().observe(this, new Observer<MovieApiResource>() {
            @Override
            public void onChanged(@Nullable MovieApiResource movieResponse) {
                mSwipeRefresh.setRefreshing(false);
                // Handles different responses
                if (movieResponse != null) {
                    switch (movieResponse.getStatus()) {
                        case SUCCESS:
                            Log.i(TAG, "Updating the adapter");
                            mMovieAdapter.setMoviesList(movieResponse.getData());
                            break;
                        case ERROR:
                            // Inform the user that the network request failed
                            Toast.makeText(MainActivity.this, getString(R.string.apiError),
                                    Toast.LENGTH_LONG).show();
                            Log.e(TAG, "Error is " + movieResponse.getError().toString());
                            break;
                        default:
                            break;
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.appbar, menu);

        // Creates the mSpinner dropdown menu
        MenuItem item = menu.findItem(R.id.action_dropdown);
        this.mSpinner = (Spinner) item.getActionView();

        mSpinner.setOnItemSelectedListener(this);

        // Create the mSpinner's adapter
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.dropdown_array, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);

        // Check for saved selection
        if (this.mBundle != null) {
            mSpinner.setSelection(this.mBundle.getInt(BUNDLE_POSITION, 0));
        }

        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        // Makes sure data isn't redundentally refreshed
        if (isLoading) {
            isLoading = false;
        } else {
            Log.i(TAG, "Refreshing data on spinner selection");
            refreshMovieData(pos);
        }
    }

    /**
     * This has to be implemented
     * @param adapterView
     */
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) { }


    /**
     * Launches the detail activity to display information about a selected movie
     * @param position the position of the selected movie in the adapter
     */
    @Override
    public void onClick(int position) {
        Movie selectedMovie =  mMovieAdapter.getItemAtPosition(position);
        Intent detailIntent = new Intent(MainActivity.this, MovieDetails.class);
        detailIntent.putExtra("movie", selectedMovie);
        startActivity(detailIntent);
    }

    /**
     * This is called when the user performs a vertical swipe on the list of movies.
     */
    @Override
    public void onRefresh() {
        Log.i(TAG, "Refreshing data on swipe");
        refreshMovieData(mSpinner.getSelectedItemPosition());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.i(TAG,"Saving mSpinner selection");
        outState.putInt(BUNDLE_POSITION, this.mSpinner.getSelectedItemPosition());
        outState.putString(BUNDLE_VALUE, mSpinner.getSelectedItem().toString());
        super.onSaveInstanceState(outState);
    }


    /**
     * Tells the ViewModel to refresh the data
     * @param pos the selected position in the spinner
     */
    private void refreshMovieData(int pos) {
        // Get the search term based off the selected position
        String sortTerm;
        switch(pos) {
            case 0:
                sortTerm = MovieUtils.MOST_POPULAR;
                break;
            case 1:
                sortTerm = MovieUtils.TOP_RATED;
                break;
            default:
                sortTerm = MovieUtils.MOST_POPULAR;
        }
        // refresh the data in the ViewModel
        mViewModel.refreshData(sortTerm);
    }

}
