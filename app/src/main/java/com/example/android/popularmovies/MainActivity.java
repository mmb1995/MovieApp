package com.example.android.popularmovies;

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

import com.example.android.popularmovies.Utils.Movie;
import com.example.android.popularmovies.Utils.MovieJSONHelper;
import com.example.android.popularmovies.Utils.MovieUtils;

import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String TAG = "Main_Activity";

    private RecyclerView mRecyclerView;
    private ArrayList<Movie> mMoviesList;
    private MovieAdapter mMovieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        // Begins an AsyncTask to query theMovieDB
        startBackgroundTask(MovieUtils.MOST_POPULAR);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.appbar, menu);

        // Creates the spinner dropdown menu
        MenuItem item = menu.findItem(R.id.action_dropdown);
        Spinner spinner = (Spinner) item.getActionView();

        spinner.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.dropdown_array,
                android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
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
        startBackgroundTask(sortTerm);
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
