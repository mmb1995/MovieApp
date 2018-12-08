package com.example.android.popularmovies.remote;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

/**
 * A generic wrapper class for api responses that holds the status of the response, and the
 * data returned by the response.
 */
public class MovieApiResource<T> {
    private List<T> data;
    private final Status status;
    private Throwable error;

    private MovieApiResource(@Nullable List<T> data, @NonNull Status status,
                            @Nullable Throwable error) {
        this.data = data;
        this.status = status;
        this.error = error;
    }

    public static <T>MovieApiResource<T> success(@NonNull List<T> data) {
        return new MovieApiResource<T>(data, Status.SUCCESS, null);
    }

    public static <T>MovieApiResource<T> error(Throwable error) {
        return new MovieApiResource<T>(null, Status.ERROR, error);
    }

    public List<T> getData() {
        return data;
    }

    public Status getStatus() {
        return status;
    }

    public void setData(List<T> data) {
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
