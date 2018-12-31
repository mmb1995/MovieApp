package com.example.android.popularmovies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.android.popularmovies.Utils.MovieUtils;
import com.example.android.popularmovies.fragment.MovieListFragment;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener,
        HasSupportFragmentInjector, SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "Main_Activity";

    // Key for selected mSpinner value
    private static final String BUNDLE_POSITION = "spinnerPos";
    private static final String BUNDLE_VALUE = "spinnerVal";

    private SwipeRefreshLayout mSwipeRefresh;
    private Spinner mSpinner;
    private Bundle mBundle;
    private boolean isRestored;


    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AndroidInjection.inject(this);

        // Check if any data was passed into the activity from a previous state
        if (savedInstanceState != null) {
            Log.i(TAG, "Restoring previous state");
            this.mBundle = savedInstanceState;
            isRestored = true;
        }

        mSwipeRefresh = findViewById(R.id.refresh);
        mSwipeRefresh.setOnRefreshListener(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
        int pos;
        if (this.mBundle != null) {
            pos = this.mBundle.getInt(BUNDLE_POSITION);
        } else {
            pos = 0;
        }
        mSpinner.setSelection(pos);
        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        getSelectedValue(pos);
    }

    /**
     * This has to be implemented
     * @param adapterView
     */
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) { }

    @Override
    public void onRefresh() {
        Log.i(TAG,"refreshing data");
        mSwipeRefresh.setRefreshing(false);
        getSelectedValue(mSpinner.getSelectedItemPosition());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.i(TAG,"Saving mSpinner selection");
        outState.putInt(BUNDLE_POSITION, this.mSpinner.getSelectedItemPosition());
        outState.putString(BUNDLE_VALUE, mSpinner.getSelectedItem().toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }


    /**
     * Called when the User clicks on an item in the spinner
     * @param pos the selected position in the spinner
     */
    private void getSelectedValue(int pos) {
        // Get the search term based off the selected position
        Bundle args = new Bundle();
        switch(pos) {
            case 0:
                args.putString(MovieListFragment.BUNDLE_SEARCH_KEY, MovieUtils.MOST_POPULAR);
                break;
            case 1:
                args.putString(MovieListFragment.BUNDLE_SEARCH_KEY, MovieUtils.TOP_RATED);
                break;
            case 2:
                args.putString(MovieListFragment.BUNDLE_SEARCH_KEY, MovieUtils.NOW_PLAYING);
                break;
            case 3:
                args.putString(MovieListFragment.BUNDLE_SEARCH_KEY, MovieUtils.COMING_SOON);
                break;
            case 4:
                args.putString(MovieListFragment.BUNDLE_SEARCH_KEY, MovieUtils.FAVORITES);
                break;
            default:
                args.putString(MovieListFragment.BUNDLE_SEARCH_KEY, MovieUtils.MOST_POPULAR);
        }
        createMovieListFragment(args);
    }

    /**
     * Creates a fragment to display a grid of movie posters
     * @param args A bundle to pass into the fragment containing the search term selected from the spinner
     */
    private void createMovieListFragment(Bundle args) {
        if (isRestored) {
            // Prevents new fragment from being created after rotation
            Log.i(TAG, "fragment restored from previous state");
            isRestored = false;
        } else {
            Log.i(TAG, "Creating fragment");
            Fragment fragment = new MovieListFragment();
            fragment.setArguments(args);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.commit();
        }
    }

}
