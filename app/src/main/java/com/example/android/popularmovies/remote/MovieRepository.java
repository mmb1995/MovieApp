package com.example.android.popularmovies.remote;

import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.example.android.popularmovies.Utils.MovieUtils;
import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.model.MovieResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieRepository {

    private static final String TAG = "MovieRepository";


    private MovieApiService mMovieApiService;

    // For singleton purposes
    private static MovieRepository mInstance;

    // This is only called if this class has not previously been instantiated
    private MovieRepository(MovieApiService service) {
        this.mMovieApiService = service;
    }

    // Singleton constructor to ensure only one instance of MovieRepository is created
    public static MovieRepository getInstance() {
        if (mInstance == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(MovieUtils.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            MovieApiService apiService = retrofit.create(MovieApiService.class);
            return new MovieRepository(apiService);
        }
        return mInstance;
    }


    public MutableLiveData<List<Movie>> getMovies(String searchTerm) {

        // This will hold the response from the network request
        final MutableLiveData<List<Movie>> data = new MutableLiveData<>();

        // Calls the service to make a request to theMovieDB
        mMovieApiService.getMovies(searchTerm, MovieUtils.API_KEY).enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                Log.i(TAG, response.toString());
                data.setValue(response.body().getMovies());
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                //
            }
        });

        return data;

        // COMPLETED add Retrofit logic to query theMovieDB
    }
}
