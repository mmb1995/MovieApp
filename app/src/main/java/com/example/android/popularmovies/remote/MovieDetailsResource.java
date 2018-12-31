package com.example.android.popularmovies.remote;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class MovieDetailsResource<T> {
    @NonNull
    public final Status status;
    @Nullable
    public final T data;
    @Nullable
    private final String message;
    private MovieDetailsResource(@NonNull Status status, @Nullable T data,
                                 @Nullable String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public static <T> MovieDetailsResource<T> success(@NonNull T data) {
        return new MovieDetailsResource<>(Status.SUCCESS, data, null);
    }

    public static <T> MovieDetailsResource<T> error(String msg, @Nullable T data) {
        return new MovieDetailsResource<>(Status.ERROR, data, msg);
    }

    public static <T> MovieDetailsResource<T> loading(@Nullable T data) {
        return new MovieDetailsResource<>(Status.LOADING, data, null);
    }

    public enum Status { SUCCESS, ERROR, LOADING }
}


