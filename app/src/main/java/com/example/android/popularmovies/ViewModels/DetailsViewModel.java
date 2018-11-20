package com.example.android.popularmovies.ViewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.example.android.popularmovies.model.MovieReview;
import com.example.android.popularmovies.model.MovieTrailer;
import com.example.android.popularmovies.remote.MovieRepository;

import java.util.List;

public class DetailsViewModel extends ViewModel {
    private static final String TAG = "DetailsViewModel";

    // Holds the list of trailers returned the repository
    private MutableLiveData<List<MovieTrailer>> mMovieTrailers;

    // Holds the list of reviews returned by the repository
    private MutableLiveData<List<MovieReview>> mMovieReviews;

    // Reference to the repository used to collect information through network requests
    private final MovieRepository mMovieRepository;


    public DetailsViewModel() {
        this.mMovieRepository = MovieRepository.getInstance();
    }

    /**
     * This is called after the activity receives the instance of the ViewModel from the factory
     * provider. This will set the id for the movie featured in the MovieDetails activity and will
     * only query the repository if the data has not already been collected.
     * @param movieId an integer used to fetch information about this movie from theMovieDb
     */
    public void init(int movieId) {
        if (mMovieTrailers == null) {
            mMovieTrailers = new MutableLiveData<>();
            loadMovieTrailers(movieId);
        }

        if(mMovieReviews == null) {
            mMovieReviews = new MutableLiveData<>();
            loadMovieReviews(movieId);
        }

    }

    /**
     * Returns the list containing information about the movie trailers
     * @return list of MovieTrailers
     */
    public LiveData<List<MovieTrailer>> getMovieTrailers() {
        return mMovieTrailers;
    }

    /**
     * Returns a list of reviews for the given movie
     * @return list of MovieReviews
     */
    public LiveData<List<MovieReview>> getMovieReviews() {
        return mMovieReviews;
    }

    /**
     * Calls the repository to fetch the movie trailers associated with a particular movie
     * @param id the id representing the movie being looked at in the details activity
     */
    private void loadMovieTrailers(int id) {
        mMovieRepository.getTrailers(this.mMovieTrailers, id);
    }

    private void loadMovieReviews(int id) {
        mMovieRepository.getReviews(this.mMovieReviews, id);
    }
}
