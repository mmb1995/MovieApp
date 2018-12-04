package com.example.android.popularmovies.remote;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.android.popularmovies.model.Movie;

import java.util.List;

/**
 * A wrapper class for api responses that holds the status of the response, and the
 * data returned by the response.
 */
public class MovieApiResource {
    private List<Movie> data;
    private final Status status;
    private Throwable error;

    private MovieApiResource(@Nullable List<Movie> data, @NonNull Status status,
                            @Nullable Throwable error) {
        this.data = data;
        this.status = status;
        this.error = error;
    }

    public static MovieApiResource success(@NonNull List<Movie> data) {
        return new MovieApiResource(data, Status.SUCCESS, null);
    }

    public static MovieApiResource error(Throwable error) {
        return new MovieApiResource(null, Status.ERROR, error);
    }

    public List<Movie> getData() {
        return data;
    }

    public Status getStatus() {
        return status;
    }

    public void setData(List<Movie> data) {
        this.data = data;
    }

    public Throwable getError() {
        return error;
    }

    public void setError(Throwable error) {
        this.error = error;
    }

    public enum Status {SUCCESS, ERROR, LOADING}
}
