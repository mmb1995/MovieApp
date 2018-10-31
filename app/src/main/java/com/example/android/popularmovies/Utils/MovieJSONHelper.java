package com.example.android.popularmovies.Utils;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MovieJSONHelper {

    private static final String TAG = "MovieJSONHelper";

    /**
     * Parses the query response from theMovieDB API
     * @param response A string representing the JSON response from the API
     * @return An arrayList of Movie objects
     */
    public static ArrayList<Movie> getMovieDataFromJSON(String response) {
        ArrayList<Movie> movies = new ArrayList<Movie>();

        try {
            // Attempt to parse JSON
            JSONObject jsonResponse = new JSONObject(response);
            JSONArray jsonResults = jsonResponse.getJSONArray("results");

            // Iterate over every movie included in the response
            for (int i = 0; i < jsonResults.length(); i++) {
                JSONObject movieInfo = jsonResults.getJSONObject(i);

                String title = movieInfo.getString("title");
                String description = movieInfo.getString("overview");
                String rating = "" + movieInfo.getDouble("vote_average");
                String releaseDate = movieInfo.getString("release_date");
                String posterPath = MovieUtils.BASE_IMAGE_URL + movieInfo.getString("poster_path");

                Movie movie = new Movie(title, description, posterPath, rating, releaseDate);
                movies.add(movie);


            }
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing json", e); // Android log the error
        }

        return movies;
    }

}
