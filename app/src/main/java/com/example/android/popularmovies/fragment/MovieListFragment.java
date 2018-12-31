package com.example.android.popularmovies.fragment;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.android.popularmovies.MovieDetails;
import com.example.android.popularmovies.R;
import com.example.android.popularmovies.ViewModels.FactoryViewModel;
import com.example.android.popularmovies.ViewModels.MovieViewModel;
import com.example.android.popularmovies.adapter.MovieAdapter;
import com.example.android.popularmovies.adapter.RecyclerViewClickListener;
import com.example.android.popularmovies.model.Movie;

import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.AndroidSupportInjection;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieListFragment extends Fragment implements RecyclerViewClickListener {
    private static final String TAG = "MovieListFragment";
    public static final String BUNDLE_SEARCH_KEY = "searchKey";

    @BindView(R.id.rvMoviePosters)
    RecyclerView mPosterRecyclerView;

    @Inject
    public FactoryViewModel mFactoryViewModel;

    private MovieAdapter mAdapter;
    private MovieViewModel mMovieViewModel;

    private String mSearchTerm;

    public MovieListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mSearchTerm = Objects.requireNonNull(getArguments()).getString(BUNDLE_SEARCH_KEY);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_movie_list, container, false);
        ButterKnife.bind(this, rootView);
        setUpPosterList();
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        AndroidSupportInjection.inject(this);
        Log.d(TAG, "Getting view model");
        this.mMovieViewModel = ViewModelProviders.of(this, mFactoryViewModel).get(MovieViewModel.class);
        this.mMovieViewModel.init(this.mSearchTerm);
        configureViewModelListener();
    }

    /**
     * Launches the details activity to display information about the selected movie
     * @param position
     */
    @Override
    public void onClick(int position) {
        Movie selectedMovie =  mAdapter.getItemAtPosition(position);
        Intent detailIntent = new Intent(getActivity(), MovieDetails.class);
        detailIntent.putExtra(MovieDetails.BUNDLE_ID, selectedMovie.getId());
        startActivity(detailIntent);
    }

    private void setUpPosterList() {
        // Checks the orientation to decide how to display the grid
        int orientation = getResources().getConfiguration().orientation;
        GridLayoutManager mGridLayoutManager;
        if(orientation == Configuration.ORIENTATION_PORTRAIT) {
            mGridLayoutManager = new GridLayoutManager(getContext(),
                    2);
        } else {
            mGridLayoutManager = new GridLayoutManager(getContext(),
                    3);
        }
        this.mAdapter= new MovieAdapter(getContext(), this);
        this.mPosterRecyclerView.setLayoutManager(mGridLayoutManager);
        this.mPosterRecyclerView.setAdapter(this.mAdapter);
    }

    /**
     * Sets up a listener that will update the ui with the data returned by the ViewModel
     */
    private void configureViewModelListener() {
        this.mMovieViewModel.getMovieData().observe(this, response -> {
            if (response != null) {
                switch (response.getStatus()) {
                    case SUCCESS:
                        Log.i(TAG, "displaying movie data");
                        this.mAdapter.setMoviesList(response.getData());
                        break;
                    case ERROR:
                        Log.i(TAG, response.getError().toString());
                        Toast.makeText(getContext(), getString(R.string.apiError),
                                Toast.LENGTH_SHORT).show();
                        break;
                    case LOADING:
                        break;
                    default:
                        break;
                }
            }
        });
    }
}
