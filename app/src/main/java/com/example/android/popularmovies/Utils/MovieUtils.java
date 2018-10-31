package com.example.android.popularmovies.Utils;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class MovieUtils {
    private static final String TAG = "Network Request";
    private static final String BASE_URL = "https://api.themoviedb.org/3/discover/movie?page=1&include_video=false&include_adult=false&sort_by=vote_average.desc&language=en-US";
    private static final String SORT_BY_PARAM = "sort_by";
    private static final String API_PARAM = "api_key";
    private static final String API_KEY = "";

    // Sort terms
    public static final String MOST_POPULAR = "popularity.desc";
    public static final String TOP_RATED= "vote_average.desc";


    public static final String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/w185";

    public static URL buildMovieDatabaseURL(String sortTerm) {
        // builds the URI for theMovieDB
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(API_PARAM, API_KEY)
                .appendQueryParameter(SORT_BY_PARAM, sortTerm)
                .build();

        Log.i(TAG, builtUri.toString());
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "" + url);
        return url;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            Log.i(TAG,"Connection started");
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            Log.i(TAG,"Connection ended");
            urlConnection.disconnect();
        }
    }
}


