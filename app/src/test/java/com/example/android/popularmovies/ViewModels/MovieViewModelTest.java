package com.example.android.popularmovies.ViewModels;

import android.arch.lifecycle.Observer;

import com.example.android.popularmovies.Utils.MovieUtils;
import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.remote.MovieRepository;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.List;

@RunWith(JUnit4.class)
public class MovieViewModelTest {

    @Mock
    private MovieRepository movieRepository;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    Observer<List<Movie>> observer;


    private MovieViewModel viewModel;

    private List<Movie> data;

    private static final String NEW_TERM = MovieUtils.TOP_RATED;
    private static final String OLD_TERM = MovieUtils.MOST_POPULAR;


    @Before
    public void setUp() {
        //MockitoAnnotations.initMocks(movieRepository);
        viewModel = new MovieViewModel(movieRepository, MovieUtils.MOST_POPULAR);
    }

    @Test
    public void testRefreshDataUpdatesCallsRepoCorrectly() {
        viewModel.refreshData(OLD_TERM);
        viewModel.refreshData(NEW_TERM);
        Mockito.verify(movieRepository, Mockito.times(1))
                .getMovies(Mockito.any(), Mockito.eq(NEW_TERM));
    }

    @Test
    public void  testInitCallsRepoCorrectly() {

    }




}