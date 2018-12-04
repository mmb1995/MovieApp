package com.example.android.popularmovies.ViewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.example.android.popularmovies.remote.MovieApiResource;
import com.example.android.popularmovies.remote.MovieRepository;

public class MovieViewModel extends ViewModel {

    private static final String TAG = "MovieViewModel";

    // Holds data returned from network requests by the repository
    private MutableLiveData<MovieApiResource> mMovieResource;

    // Reference to the repository used to collect information through network requests
    private final MovieRepository mMovieRepository;

    // Holds the sort term selected by the user
    private String searchTerm;

    public MovieViewModel() {
        this.mMovieRepository = MovieRepository.getInstance();
    }

    /**
     * For testing
     * @param repo
     * @param term
     */
    public MovieViewModel(MovieRepository repo, String term) {
        this.mMovieRepository = repo;
        this.searchTerm = term;
        this.mMovieResource = new MutableLiveData<>();
    }

    public void init(String searchTerm) {
        Log.i(TAG,"Setting up view model");
        if (this.mMovieResource != null) {
            // Don't create a new instance if one already exists
            Log.i(TAG, "Data already present");
            return;
        }
        Log.i(TAG, "Init search value = " + searchTerm);
        this.searchTerm = searchTerm;
        mMovieResource = new MutableLiveData<>();
        loadMovieData();
    }

    // COMPLETED respond to spinner selections and get data to update after load data is called
    // Returns the list of Movie data
    public LiveData<MovieApiResource> getMovieData() {
        return mMovieResource;
    }

    // updates the searchTerm when the user selects a new term from the spinner
    public void refreshData(String searchTerm) {
        Log.i(TAG, searchTerm);
        if (!this.searchTerm.equals(searchTerm)) {
            this.searchTerm = searchTerm;
            loadMovieData();
        }
    }

    // Calls the repository and passes it the term that will be used to query theMovieDB
    private void loadMovieData() {
        //Log.i(TAG, "getting Movie data from repo");
        mMovieRepository.getMovies(this.mMovieResource, this.searchTerm);
    }
}
