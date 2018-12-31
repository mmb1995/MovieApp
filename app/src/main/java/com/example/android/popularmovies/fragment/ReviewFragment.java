package com.example.android.popularmovies.fragment;


import android.arch.lifecycle.ViewModelProviders;
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
import com.example.android.popularmovies.ViewModels.FactoryViewModel;
import com.example.android.popularmovies.ViewModels.MovieDetailsViewModel;
import com.example.android.popularmovies.adapter.MovieReviewAdapter;

import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.AndroidSupportInjection;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReviewFragment extends Fragment {
    private static final String TAG = "ReviewFragment";
    private static final String ID_KEY = "id";

    @BindView(R.id.reviewRecyclerView)
    RecyclerView mReviewRecyclerview;

    @Inject
    public FactoryViewModel mFactoryViewModel;
    private MovieReviewAdapter mReviewAdapter;

    public ReviewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_review, container, false);
        ButterKnife.bind(this, rootView);

        // Set up RecyclerView and ViewModel
        mReviewRecyclerview.setLayoutManager( new LinearLayoutManager(getActivity()));
        mReviewAdapter = new MovieReviewAdapter(getActivity());
        mReviewRecyclerview.setAdapter(mReviewAdapter);
        ViewCompat.setNestedScrollingEnabled(mReviewRecyclerview, false);
        return rootView;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.configureDagger();
        this.setUpReviews();
    }

    private void configureDagger() {
        AndroidSupportInjection.inject(this);
    }

    private void setUpReviews() {
        int mMovieId = Objects.requireNonNull(getArguments()).getInt(ID_KEY);
        Log.i(TAG, "movieId = " + mMovieId);
        MovieDetailsViewModel mReviewViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity()), mFactoryViewModel).get(MovieDetailsViewModel.class);
        mReviewViewModel.initReviews(mMovieId);

        // Set up observer and callback
        mReviewViewModel.getMovieReviews().observe(this, movieReviewResource -> {
            if (movieReviewResource != null) {
                switch (movieReviewResource.getStatus()) {
                    case SUCCESS:
                        mReviewAdapter.setMoviesReviews(movieReviewResource.getData());
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


