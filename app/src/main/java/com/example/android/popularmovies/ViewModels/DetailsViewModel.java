package com.example.android.popularmovies.ViewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.example.android.popularmovies.model.MovieTrailer;
import com.example.android.popularmovies.remote.MovieRepository;

import java.util.List;

public class DetailsViewModel extends ViewModel {
    private static final String TAG = "DetailsViewModel";

    // Holds data returned from network requests by the repository
    private MutableLiveData<List<MovieTrailer>> mMovieTrailers;

    // Reference to the repository used to collect information through network requests
    private MovieRepository mMovieRepository;


    public DetailsViewModel() {
        this.mMovieRepository = MovieRepository.getInstance();
    }

    /**
     * This is called after the activity receives the instance of the ViewModel from the factory
     * provider. This will set the id for the movie featured in the MovieDetails activity
     * @param movieId an integer used to fetch information about this movie from theMovieDb
     */
    public void init(int movieId) {
        if (mMovieTrailers != null) {
            // Only initialize once per the activity's lifecycle
            return;
        }
        mMovieTrailers = new MutableLiveData<>();
        loadMovieTrailers(movieId);
    }

    /**
     * Returns the list containing information about the movie trailers
     * @return
     */
    public LiveData<List<MovieTrailer>> getMovieTrailers() {
        return mMovieTrailers;
    }

    /**
     * Calls the repository to fetch the movie trailers associated with a particular movie
     * @param id the id representing the movie being looked at in the details activity
     */
    public void loadMovieTrailers(int id) {
        mMovieRepository.getTrailers(this.mMovieTrailers, id);
    }
}
