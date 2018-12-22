package com.example.android.popularmovies.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.android.popularmovies.model.Movie;

import java.util.List;

@Dao
public interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addMovie(Movie movie);

    @Insert
    void addListOfMovies(List<Movie> movieList);

    @Query("SELECT * FROM Movies WHERE favorite = 1")
    LiveData<List<Movie>> getFavoriteMovies();

    @Query("SELECT * FROM Movies WHERE id = :movieId LIMIT 1")
    Movie getMovieById(int movieId);

    @Delete
    void removeMovie(Movie movie);
}
