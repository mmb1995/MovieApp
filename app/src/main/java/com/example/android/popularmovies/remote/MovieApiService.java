package com.example.android.popularmovies.remote;

import com.example.android.popularmovies.model.MovieResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieApiService {

    @GET("movie/{search_term}")
    Call<MovieResponse> getMovies(@Path("search_term") String searchTerm,
                                  @Query("api_key") String apiKey);
}
