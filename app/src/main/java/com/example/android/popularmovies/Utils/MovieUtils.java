package com.example.android.popularmovies.Utils;

/**
 * Util class that stores constants needed for Network Requests. These values are needed in
 * multiple classes so they are stored here so that any class that needs them can get them
 * Note: **Add your theMovieDb api key to the API_KEY constant**
 */
public class MovieUtils {
    // Retrofit resources
    public static final String BASE_URL = "https://api.themoviedb.org/3/";
    public static final String API_KEY = "";

    // Sort terms
    public static final String MOST_POPULAR = "popular";
    public static final String TOP_RATED = "top_rated";

    // Image URL Resources
    public static final String BASE_IMAGE_URL = "https://image.tmdb.org/t/p/";
    public static final String POSTER_IMAGE_SIZE_MAIN_GRID = "w185";
    public static final String POSTER_IMAGE_SIZE_DETAIL = "w780";

}


