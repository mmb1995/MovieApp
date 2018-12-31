package com.example.android.popularmovies.ViewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.remote.MovieDetailsResource;
import com.example.android.popularmovies.remote.MovieRepository;
import com.example.android.popularmovies.remote.MovieResource;

import javax.inject.Inject;

public class MovieDetailsViewModel extends ViewModel {
    private static final String TAG = "MovieDetailsViewModel";

    private LiveData<MovieDetailsResource> movie;

    // Holds movie trailer data returned by the repo
    private MutableLiveData<MovieResource> mMovieTrailersList;

    // hold movie review data returned by the repo
    private MutableLiveData<MovieResource> mMovieReviewsList;


    // Reference to the repository used to collect information through network request
    private final MovieRepository mMovieRepository;


    // Instructs Dagger 2 to provide the MovieRepository parameter
    @Inject
    public MovieDetailsViewModel(MovieRepository movieRepo) {
        this.mMovieRepository = movieRepo;
    }

    public void init(int movieId) {
        if (this.movie != null) {
            Log.i(TAG, "Movie already present");
            return;
        }
        movie = mMovieRepository.getMovieById(movieId);
    }

    public LiveData<MovieDetailsResource> getMovie() {
        return this.movie;
    }

    /**
     * Tells the ViewModel to ask the repo for the trailers associated with the given movie
     * @param movieId the movieDb id assigned to the selected movie
     */
    public void initTrailers(int movieId) {
        if (mMovieTrailersList != null) {
            Log.i(TAG, "Trailers already present");
            return;
        }
        mMovieTrailersList = new MutableLiveData<>();
        loadMovieTrailers(movieId);
    }

    /**
     * Tells the ViewModel to ask the repo for the reviews associated with the given movie
     * @param movieId the movieDb id assigned to the selected movie
     */
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
    public LiveData<MovieResource> getMovieTrailers() {
        return mMovieTrailersList;
    }

    /**
     * Returns the MovieApiResource that contains the user reviews returned from the repo
     * @return
     */
    public LiveData<MovieResource> getMovieReviews() {return mMovieReviewsList;}

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

    public void addFavorite(Movie movie) {
        mMovieRepository.addMovie(movie);
    }

    public void removeFavorite(Movie movie) {
        mMovieRepository.deleteMovie(movie);
    }

}
