package com.example.android.popularmovies.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.Utils.MovieUtils;
import com.example.android.popularmovies.ViewModels.FactoryViewModel;
import com.example.android.popularmovies.ViewModels.MovieDetailsViewModel;
import com.example.android.popularmovies.adapter.MovieTrailerAdapter;
import com.example.android.popularmovies.adapter.RecyclerViewClickListener;
import com.example.android.popularmovies.model.MovieTrailer;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.AndroidSupportInjection;


public class TrailerFragment extends Fragment implements RecyclerViewClickListener {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TAG = "TrailerFragment";
    private static final String ID_KEY = "id";

    @BindView(R.id.trailerRecyclerView) RecyclerView mTrailerRecyclerView;

    @Inject
    public FactoryViewModel mFactoryViewModel;
    private int mMovieId;
    private MovieDetailsViewModel mTrailerViewModel;
    private MovieTrailerAdapter mTrailerAdapter;

    public TrailerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_trailer, container, false);
        ButterKnife.bind(this, rootView);

        // Set up RecyclerView and ViewModel
        mTrailerRecyclerView.setLayoutManager( new LinearLayoutManager(getActivity()));
        mTrailerAdapter = new MovieTrailerAdapter(getActivity(), this);
        mTrailerRecyclerView.setAdapter(mTrailerAdapter);
        ViewCompat.setNestedScrollingEnabled(mTrailerRecyclerView, false);;
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.configureDagger();
        this.setUpTrailers();
    }


    @Override
    public void onClick(int position) {
        MovieTrailer selectedTrailer = mTrailerAdapter.getItemAtPosition(position);
        final String trailerUrl = MovieUtils.BASE_YOUTUBE_URL + selectedTrailer.getKey();
        Log.i(TAG, "Starting intent to play video");

        // Builds an implicit intent to play the selected trailer
        Intent playTrailerIntent = new Intent(Intent.ACTION_VIEW);
        playTrailerIntent.setData(Uri.parse(trailerUrl));
        Log.i(TAG, "trailer url = " + trailerUrl);
        startActivity(Intent.createChooser(playTrailerIntent, "Complete action using"));
    }

    private void configureDagger() {
        AndroidSupportInjection.inject(this);
    }

    private void setUpTrailers() {
        this.mMovieId = getArguments().getInt(ID_KEY);
        Log.i(TAG, "movieId = " + mMovieId);
        mTrailerViewModel = ViewModelProviders.of(getActivity(),mFactoryViewModel ).get(MovieDetailsViewModel.class);
        mTrailerViewModel.initTrailers(mMovieId);

        // Set up observer and callback
        mTrailerViewModel.getMovieTrailers().observe(this, movieTrailerResource -> {
            if (movieTrailerResource != null) {
                switch (movieTrailerResource.getStatus()) {
                    case SUCCESS:
                        mTrailerAdapter.setMoviesList(movieTrailerResource.getData());
                        break;
                    case ERROR:
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

