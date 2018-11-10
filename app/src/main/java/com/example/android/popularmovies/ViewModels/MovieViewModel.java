package com.example.android.popularmovies.ViewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.remote.MovieRepository;

import java.util.List;

public class MovieViewModel extends ViewModel {

    // Holds data returned from network requests by the repository
    private MutableLiveData<List<Movie>> mMovieList;

    // Reference to the repository used to collect information through network requests
    private MovieRepository mMovieRepository;

    // Holds the sort term selected by the user
    private String searchTerm;

    public MovieViewModel() {
        this.mMovieRepository = MovieRepository.getInstance();
    }

    // TODO respond to spinner selections
    // Returns the data from previous configuration or calls the repository to fetch new data
    public LiveData<List<Movie>> getMovieData() {
        if (mMovieList == null) {
            mMovieList = new MutableLiveData<>();
            loadMovieData();
        }
        return mMovieList;
    }

    // updates the searchTerm when the user selects a new term from the spinner
    public void  setSearchTerm(String searchTerm) {
        if (this.searchTerm != searchTerm) {
            this.searchTerm = searchTerm;
            loadMovieData();
        }
    }

    // Calls the repository and passes it the term that will be used to query theMovieDB
    private void loadMovieData() {
        mMovieList = mMovieRepository.getMovies(this.searchTerm);
    }
}
