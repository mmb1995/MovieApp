package com.example.android.popularmovies.di;

import com.example.android.popularmovies.MainActivity;
import com.example.android.popularmovies.MovieDetails;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityModule {
    @ContributesAndroidInjector
    abstract MainActivity contributeMainActivity();

    @ContributesAndroidInjector
    abstract MovieDetails contributeMovieDetail();
}
