package com.example.android.popularmovies.ViewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.remote.MovieApiResource;
import com.example.android.popularmovies.remote.MovieRepository;

import java.util.List;

import javax.inject.Inject;

public class MovieViewModel extends ViewModel {

    private static final String TAG = "MovieViewModel";

    // Holds data returned from network requests by the repository
    private MutableLiveData<MovieApiResource> mMovieResource;

    // Reference to the repository used to collect information through network requests
    private final MovieRepository mMovieRepository;

    // Holds favorite movies
    private LiveData<List<Movie>> mFavoritesList;

    // Tells dagger 2 to inject the MovieRepository parameter
    @Inject
    public MovieViewModel(MovieRepository movieRepository) {
        this.mMovieRepository = movieRepository;
    }

    /**
     * For testing
     * @param repo
     * @param term
     */
    public MovieViewModel(MovieRepository repo, String term) {
        this.mMovieRepository = repo;
        this.mMovieResource = new MutableLiveData<>();
    }

    /**
     * This needs to be called after an activity or fragment receives  an instance of the viewmodel,
     * this is used to set up the data stored in the viewmodel.
     * @param searchTerm
     */
    public void init(String searchTerm) {
        Log.i(TAG,"Setting up view model");
        if (this.mMovieResource != null) {
            // Don't create a new instance if one already exists
            Log.i(TAG, "Data already present");
            return;
        }
        Log.i(TAG, "Init search value = " + searchTerm);
        mMovieResource = new MutableLiveData<>();
        loadMovieData(searchTerm);
    }

    // COMPLETED respond to spinner selections and get data to update after load data is called
    // Returns the list of Movie data
    public LiveData<MovieApiResource> getMovieData() {
        return mMovieResource;
    }

    // updates the searchTerm when the user selects a new term from the spinner
    public void refreshData(String searchTerm) {
        Log.i(TAG, searchTerm);
        loadMovieData(searchTerm);
    }

    // Calls the repository and passes it the term that will be used to query theMovieDB
    private void loadMovieData(String searchTerm) {
        //Log.i(TAG, "getting Movie data from repo");
        mMovieRepository.getMovies(this.mMovieResource, searchTerm);
    }

    public LiveData<List<Movie>> getFavoriteMovies() {
        Log.i(TAG, "Getting favorite movies");
        if (mFavoritesList == null) {
            mFavoritesList = mMovieRepository.getFavorites();
        }
        return this.mFavoritesList;
    }
}
