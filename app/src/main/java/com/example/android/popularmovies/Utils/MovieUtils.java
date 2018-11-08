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
    // Uri resources
    private static final String TAG = "Network Request";
    private static final String BASE_URL = "https://api.themoviedb.org/";
    private static final String DEFAULT_PATH = "page=1&language=en-US";
    private static final String API_PARAM = "api_key";
    private static final String API_KEY = "";
    private static final String PAGE_PARAM = "page";
    private static final String PAGE_VALUE = "1";
    private static final String LANGUAGE_PARAM = "language";
    private static final String LANGUAGE_VALUE = "en-US";

    // Sort terms
    public static final String MOST_POPULAR = "3/movie/popular";
    public static final String TOP_RATED = "3/movie/top_rated";


    // Image URL Resources
    public static final String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/";
    public static final String POSTER_IMAGE_SIZE_MAIN = "w185";
    public static final String POSTER_IMAGE_SIZE_DETAIL = "w780";

    public static URL buildMovieDatabaseURL(String sortTerm) {
        // builds the URI for theMovieDB
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .path(sortTerm)
                .appendQueryParameter(PAGE_PARAM, PAGE_VALUE)
                .appendQueryParameter(LANGUAGE_PARAM, LANGUAGE_VALUE)
                .appendQueryParameter(API_PARAM, API_KEY)
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


