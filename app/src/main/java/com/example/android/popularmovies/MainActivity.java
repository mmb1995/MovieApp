package com.example.android.popularmovies;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
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

import com.example.android.popularmovies.Utils.Movie;
import com.example.android.popularmovies.Utils.MovieJSONHelper;
import com.example.android.popularmovies.Utils.MovieUtils;

import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String TAG = "Main_Activity";

    // Key for selected spinner value
    private static final String SPINNER_KEY = "spinner";

    private RecyclerView mRecyclerView;
    private ArrayList<Movie> mMoviesList;
    private MovieAdapter mMovieAdapter;
    private Bundle savedInstanceState;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if any data was passed into the activity from a previous state
        if (savedInstanceState != null) {
            this.savedInstanceState = savedInstanceState;
        }

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Sets up the GridLayoutManager for the RecyclerView
        mRecyclerView = findViewById(R.id.rvMoviePosters);
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(MainActivity.this, 2);
        mRecyclerView.setLayoutManager(mGridLayoutManager);


        Log.i(TAG,"Setting up RecyclerView");
        mMovieAdapter = new MovieAdapter(MainActivity.this);
        mRecyclerView.setAdapter(mMovieAdapter);

        if (this.savedInstanceState == null && isConnected()) {
            // Begins an AsyncTask to query theMovieDB if there is no saved selection

            startBackgroundTask(MovieUtils.MOST_POPULAR);
        } else {
            // No network connection
            Toast.makeText(this,"No Internet Connection Detected", Toast.LENGTH_LONG).show();
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
            startBackgroundTask(sortTerm);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Do nothing
        return;
    }

    /**
     * Begin an AsyncTask that will query theMovieDB
     * @param sortTerm the value to sort the results by
     */
    public void startBackgroundTask(String sortTerm) {
        URL movieDbUrl = MovieUtils.buildMovieDatabaseURL(sortTerm);
        Log.i(TAG,"url: " + movieDbUrl);
        new MovieAsyncTask().execute(movieDbUrl);
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


    public class MovieAsyncTask extends AsyncTask<URL, Void, ArrayList<Movie>> {


        @Override
        protected ArrayList<Movie> doInBackground(URL... params) {
            URL movieDbUrl = params[0];
            Log.i(TAG, "Background task starting. This is the current url" + movieDbUrl);
            // This will Store the Json Response
            String movieDbJsonResponse = null;

            // Attempt to query theMovieDb API
            try {
                movieDbJsonResponse = MovieUtils.getResponseFromHttpUrl(movieDbUrl);
                Log.i(TAG,"Json response:" + movieDbJsonResponse);
                ArrayList<Movie> movieList = MovieJSONHelper.getMovieDataFromJSON(movieDbJsonResponse);

                // Return the list of movie data
                return movieList;

            } catch(Exception e) {
                Log.e(TAG, "Request Failed");
                e.printStackTrace();
                return null;
            }
        }

        /**
         * Updates the MovieAdapter with the most recent data received from the network request
         * @param movieJsonList The response from theMovieDb API
         */
        @Override
        protected void onPostExecute(ArrayList<Movie> movieJsonList) {
            mMovieAdapter.setMoviesList(movieJsonList);
            mMovieAdapter.notifyDataSetChanged();
        }
    }

}
