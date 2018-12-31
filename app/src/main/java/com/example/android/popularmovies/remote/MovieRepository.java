package com.example.android.popularmovies.remote;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.android.popularmovies.Utils.MovieUtils;
import com.example.android.popularmovies.database.MovieDao;
import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.model.MovieResponse;
import com.example.android.popularmovies.model.MovieReviewResponse;
import com.example.android.popularmovies.model.MovieTrailerResponse;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * This class performs network requests on a background thread, and is the
 * single source of truth for fetching data
 */
@Singleton
public class MovieRepository {

    private static final String TAG = "MovieRepository";

    private final MovieApiService mMovieApiService;
    private final MovieDao mMovieDao;
    private final Executor executor;

    // This is only called if this class has not previously been instantiated
    @Inject
    public MovieRepository(MovieApiService service, MovieDao movieDao, Executor executor) {
        this.mMovieApiService = service;
        this.mMovieDao = movieDao;
        this.executor = executor;
    }

    /**
     * Performs network request to fetch a list of movies from theMovieDb
     * @param searchTerm a search term to query theMovieDb by different categories
     */
    public LiveData<MovieResource> getMovies(String searchTerm) {
        // Calls the service to make a request to theMovieDB
        if (searchTerm.equals(MovieUtils.FAVORITES)) {
            return getFavorites();
        }
        final MutableLiveData<MovieResource> data = new MutableLiveData<>();
        mMovieApiService.getMovies(searchTerm, MovieUtils.API_KEY).enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(@NonNull Call<MovieResponse> call, @NonNull Response<MovieResponse> response) {
                Log.i(TAG, response.toString());
                if (response.isSuccessful()) {
                    data.setValue(MovieResource.success(Objects.requireNonNull(response.body()).getMovies()));
                } else {
                    // the response did not return valid data
                    onFailure(call, new Throwable("api key likely missing"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieResponse> call, @NonNull Throwable t) {
                call.cancel();
                data.setValue(MovieResource.error(t));
            }
        });
        return data;
    }


    public void getTrailers(final MutableLiveData<MovieResource> data, int id) {
        // calls the service to get the trailers associated with the given id
        mMovieApiService.getTrailers(id, MovieUtils.API_KEY).enqueue(new Callback<MovieTrailerResponse>() {
            @Override
            public void onResponse(@NonNull Call<MovieTrailerResponse> call, @NonNull Response<MovieTrailerResponse> response) {
                Log.i(TAG,"Trailer response: " + response.toString());
                if (response.isSuccessful()) {
                    data.setValue(MovieResource.success(Objects.requireNonNull(response.body()).getTrailers()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieTrailerResponse> call, @NonNull Throwable t) {
                call.cancel();
                t.printStackTrace();
                data.setValue(MovieResource.error(t));
            }
        });
    }

    public void getReviews(final MutableLiveData<MovieResource> data, int id) {
        // calls the service to get the user reviews associated with the given movie id
        mMovieApiService.getReviews(id, MovieUtils.API_KEY).enqueue(new Callback<MovieReviewResponse>() {
            @Override
            public void onResponse(@NonNull Call<MovieReviewResponse> call, @NonNull Response<MovieReviewResponse> response) {
                Log.i(TAG,"Review response: " + response.toString());
                if (response.isSuccessful()) {
                    data.setValue(MovieResource.success(Objects.requireNonNull(response.body()).getTrailers()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieReviewResponse> call, @NonNull Throwable t) {
                call.cancel();
                t.printStackTrace();
                data.setValue(MovieResource.error(t));
            }
        });
    }

    /**
     * Adds given movie to the database
     * @param movie
     */
    public void addMovie(final Movie movie) {
        // Runs in background thread
        Log.i(TAG, "Adding favorite to database");
        executor.execute(() -> mMovieDao.addMovie(movie));
    }

    public void deleteMovie(final Movie movie) {
        Log.i(TAG, "Removing movie from database");
        executor.execute(() -> mMovieDao.removeMovie(movie));
    }

    /**
     * Returns the users favorite movies from the database
     * @return
     */
    private LiveData<MovieResource> getFavorites() {
        // Gets the data from the database
        final LiveData<List<Movie>> source = mMovieDao.getFavoriteMovies();

        // mediator will observe the changes when notified by room
        final MediatorLiveData mediator = new MediatorLiveData();
        mediator.addSource(source, (Observer<List<Movie>>) favoriteMovies -> {
            Log.d(TAG, "Received data from database");
            MovieResource resource = MovieResource.success(favoriteMovies);
            mediator.setValue(resource);
        });
        return mediator;
    }

    /**
     * Returns an observable resource to monitor the status and data returned about the given movie
     * @param movieId theMovieDb id given to the selected movie
     * @return
     */
    public LiveData<MovieDetailsResource> getMovieById(int movieId) {
        final MutableLiveData<MovieDetailsResource> data = new MutableLiveData<>();

        //Check to see if the movie is in the database
        executor.execute(() -> {
            Movie movie = mMovieDao.getMovieById(movieId);
            if (movie != null) {
                MovieDetailsResource resource = MovieDetailsResource.success(movie);
                data.postValue(resource);
            } else {
                mMovieApiService.getMovieById(movieId, MovieUtils.API_KEY).enqueue(new Callback<Movie>() {
                    @Override
                    public void onResponse(@NonNull Call<Movie> call, @NonNull Response<Movie> response) {
                        if (response.isSuccessful()) {
                            data.postValue(MovieDetailsResource.success(Objects.requireNonNull(response.body())));
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Movie> call, @NonNull Throwable t) {
                        t.printStackTrace();
                        data.postValue(MovieDetailsResource.error(t.toString(), null));
                    }
                });
            }
        });
        return data;
    }

}
