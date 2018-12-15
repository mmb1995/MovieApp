package com.example.android.popularmovies.ViewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.example.android.popularmovies.remote.MovieApiResource;
import com.example.android.popularmovies.remote.MovieRepository;

public class MovieDetailsViewModel extends ViewModel {
    private static final String TAG = "MovieDetailsViewModel";

    // Holds movie trailer data returned by the repo
    private MutableLiveData<MovieApiResource> mMovieTrailersList;

    // hold movie review data returned by the repo
    private MutableLiveData<MovieApiResource> mMovieReviewsList;

    // Reference to the repository used to collect information through network request
    private final MovieRepository mMovieRepository;

    public MovieDetailsViewModel() {
        this.mMovieRepository = MovieRepository.getInstance();
    }

    /**
     * This is called after the activity receives the instance of the ViewModel. this will set the id
     * for the movie currently featured in the MovieDetails activity.
     */
    public void initTrailers(int movieId) {
        if (mMovieTrailersList != null) {
            Log.i(TAG, "Trailers already present");
            return;
        }
        mMovieTrailersList = new MutableLiveData<>();
        loadMovieTrailers(movieId);
    }

    public void initReviews(int movieId) {
        if (mMovieReviewsList != null) {
            Log.i(TAG, "Reviews already present");
            return;
        }
        mMovieReviewsList = new MutableLiveData<>();
        loadMovieReviews(movieId);
    }

    /**
     * Returns the MovieApiResource that contains the trailer data returned from the repo
     * @return
     */
    public LiveData<MovieApiResource> getMovieTrailers() {
        return mMovieTrailersList;
    }

    /**
     * Returns the MovieApiResource that contains the user reviews returned from the repo
     * @return
     */
    public LiveData<MovieApiResource> getMovieReviews() {return mMovieReviewsList;}

    /**
     * Calls the repo to fetch the movie trailers associated with the given movieId
     * @param movieId the id of the movie to fetch trailers for
     */
    private void loadMovieTrailers(int movieId) {
        mMovieRepository.getTrailers(this.mMovieTrailersList, movieId);
    }

    /**
     * Calls the repo to fetch the reviews associated with the given movieId
     * @param movieId the id of the movie to fetch reviews for
     */
    private void loadMovieReviews(int movieId) {
        mMovieRepository.getReviews(this.mMovieReviewsList, movieId);
    }

}
