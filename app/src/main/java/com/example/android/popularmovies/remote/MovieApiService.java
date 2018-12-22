package com.example.android.popularmovies.remote;

import com.example.android.popularmovies.Utils.MovieUtils;
import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.model.MovieResponse;
import com.example.android.popularmovies.model.MovieReviewResponse;
import com.example.android.popularmovies.model.MovieTrailerResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieApiService {

    @GET("movie/{search_term}")
    Call<MovieResponse> getMovies(@Path("search_term") String searchTerm,
                                  @Query(MovieUtils.API_PARAM) String apiKey);


    @GET("movie/{movie_id}/videos")
    Call<MovieTrailerResponse> getTrailers(@Path("movie_id") int movieId,
                                           @Query(MovieUtils.API_PARAM) String apiKey);

    @GET("movie/{movie_id}/reviews")
    Call<MovieReviewResponse> getReviews(@Path("movie_id") int movieId,
                                         @Query(MovieUtils.API_PARAM) String apiKey);

    @GET("movie/{movie_id}")
    Call<Movie> getMovieById(@Path("movie_id") int movieId,
                             @Query(MovieUtils.API_PARAM) String apiKey);

}
