package com.example.android.popularmovies.ViewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.remote.MovieRepository;

import java.util.List;

public class MovieViewModel extends ViewModel {

    private static final String TAG = "MovieViewModel";

    // Holds data returned from network requests by the repository
    private MutableLiveData<List<Movie>> mMovieList;

    // Reference to the repository used to collect information through network requests
    private final MovieRepository mMovieRepository;

    // Holds the sort term selected by the user
    private String searchTerm;

    public MovieViewModel() {
        this.mMovieRepository = MovieRepository.getInstance();
    }

    public void init(String searchTerm) {
        if (this.mMovieList != null) {
            // Don't create a new instance if one already exists
            return;
        }
        this.searchTerm = searchTerm;
        mMovieList = new MutableLiveData<>();
        loadMovieData();
    }

    // COMPLETED respond to spinner selections and get data to update after load data is called
    // Returns the list of Movie data
    public LiveData<List<Movie>> getMovieData() {
        return mMovieList;
    }

    // updates the searchTerm when the user selects a new term from the spinner
    public void refreshData(String searchTerm) {
        if (!this.searchTerm.equals(searchTerm)) {
            this.searchTerm = searchTerm;
            loadMovieData();
        }
    }


    // Calls the repository and passes it the term that will be used to query theMovieDB
    private void loadMovieData() {
        Log.i(TAG, "getting Movie data from repo");
        mMovieRepository.getMovies(this.mMovieList, this.searchTerm);
    }
}
