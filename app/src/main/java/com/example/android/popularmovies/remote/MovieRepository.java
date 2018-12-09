package com.example.android.popularmovies.remote;

import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.android.popularmovies.Utils.MovieUtils;
import com.example.android.popularmovies.model.MovieResponse;
import com.example.android.popularmovies.model.MovieTrailerResponse;

import java.util.Objects;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * This class performs network requests on a background thread, and is the
 * single source of truth for fetching data
 */
public class MovieRepository {

    private static final String TAG = "MovieRepository";


    private final MovieApiService mMovieApiService;

    // For singleton purposes
    private static MovieRepository mInstance;

    // This is only called if this class has not previously been instantiated
    private MovieRepository(MovieApiService service) {
        this.mMovieApiService = service;
    }

    // Singleton constructor to ensure only one instance of MovieRepository is created
    public static MovieRepository getInstance() {
        if (mInstance == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            OkHttpClient okHttpClient = builder.build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(MovieUtils.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();
            MovieApiService apiService = retrofit.create(MovieApiService.class);
            mInstance = new MovieRepository(apiService);
        }
        return mInstance;
    }

    /**
     * Performs network request to fetch a list of movies from theMovieDb
     * @param data LiveData object that holds the list of movies returned from the request
     * @param searchTerm a search term to query theMovieDb by different categories
     */
    public void getMovies(final MutableLiveData<MovieApiResource> data, String searchTerm) {
        // Calls the service to make a request to theMovieDB
        mMovieApiService.getMovies(searchTerm, MovieUtils.API_KEY).enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(@NonNull Call<MovieResponse> call, @NonNull Response<MovieResponse> response) {
                Log.i(TAG, response.toString());
                if (response.isSuccessful()) {
                    data.setValue(MovieApiResource.success(Objects.requireNonNull(response.body()).getMovies()));
                } else {
                    // the response did not return valid data
                    onFailure(call, new Throwable("api key likely missing"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieResponse> call, @NonNull Throwable t) {
                call.cancel();
                data.setValue(MovieApiResource.error(t));
            }
        });
        // COMPLETED add Retrofit logic to query theMovieDB
    }


    public void getTrailers(final MutableLiveData<MovieApiResource> data, int id) {
        // calls the service to get the trailers associated with the given id
        mMovieApiService.getTrailers(id, MovieUtils.API_KEY).enqueue(new Callback<MovieTrailerResponse>() {
            @Override
            public void onResponse(@NonNull Call<MovieTrailerResponse> call, @NonNull Response<MovieTrailerResponse> response) {
                Log.i(TAG,"Trailer response: " + response.toString());
                if (response.isSuccessful()) {
                    data.setValue(MovieApiResource.success(Objects.requireNonNull(response.body()).getTrailers()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieTrailerResponse> call, @NonNull Throwable t) {
                call.cancel();
                t.printStackTrace();
                data.setValue(MovieApiResource.error(t));
            }
        });
    }
}
