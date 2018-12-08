package com.example.android.popularmovies.ViewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.example.android.popularmovies.remote.MovieApiResource;
import com.example.android.popularmovies.remote.MovieRepository;

public class MovieDetailsViewModel extends ViewModel {
    private static final String TAG = "MovieDetailsViewModel";

    // Holds movie trailer data returned by the repo
    private MutableLiveData<MovieApiResource> mMovieTrailersList;

    // Reference to the repository used to collect information through network request
    private final MovieRepository mMovieRepository;

    public MovieDetailsViewModel() {
        this.mMovieRepository = MovieRepository.getInstance();
    }

    /**
     * This is called after the activity receives the instance of the ViewModel. this will set the id
     * for the movie currently featured in the MovieDetails activity.
     */
    public void init(int movieId) {
        if (mMovieTrailersList != null) {
            return;
        }
        mMovieTrailersList = new MutableLiveData<>();
        loadMovieTrailers(movieId);
    }

    /**
     * Returns the MovieApiResource that contains the trailer data returned from the repo
     * @return
     */
    public LiveData<MovieApiResource> getMovieTrailers() {
        return mMovieTrailersList;
    }

    /**
     * Calls the repo to fetch the movie trailer associated with the given id
     * @param movieId the id of the movie to fetch trailers for
     */
    private void loadMovieTrailers(int movieId) {
        mMovieRepository.getTrailers(this.mMovieTrailersList, movieId);
    }

}
