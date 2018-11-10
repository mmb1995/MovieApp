package com.example.android.popularmovies.remote;

import android.arch.lifecycle.MutableLiveData;

import com.example.android.popularmovies.Utils.MovieUtils;
import com.example.android.popularmovies.model.Movie;

import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieRepository {

    private MovieApiService mMovieApiService;

    // For singleton purposes
    private static MovieRepository mInstance;

    // This is only call if this class has not previously been instantiated
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
        final MutableLiveData<List<Movie>> data = new MutableLiveData<>();
        return data;

        // TODO add Retrofit logic to query theMovieDB
    }
}
