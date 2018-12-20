package com.example.android.popularmovies.di;

import com.example.android.popularmovies.fragment.MovieListFragment;
import com.example.android.popularmovies.fragment.ReviewFragment;
import com.example.android.popularmovies.fragment.TrailerFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FragmentModule {
    @ContributesAndroidInjector
    abstract TrailerFragment contributeTrailerFragment();

    @ContributesAndroidInjector
    abstract ReviewFragment contributeReviewFragment();

    @ContributesAndroidInjector
    abstract MovieListFragment contributeMovieListFragment();
}
