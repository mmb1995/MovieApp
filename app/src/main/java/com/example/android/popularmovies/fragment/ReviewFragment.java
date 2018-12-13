package com.example.android.popularmovies.fragment;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.ViewModels.MovieDetailsViewModel;
import com.example.android.popularmovies.adapter.MovieReviewAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReviewFragment extends Fragment {
    private static final String TAG = "ReviewFragment";
    private static final String ID_KEY = "id";

    @BindView(R.id.reviewRecyclerView)
    RecyclerView mReviewRecyclerview;

    // TODO: Rename and change types of parameters
    private int mMovieId;
    private MovieDetailsViewModel mReviewViewModel;
    private MovieReviewAdapter mReviewAdapter;

    public ReviewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mMovieId = getArguments().getInt(ID_KEY);
            Log.i(TAG, "movieId = " + mMovieId);
            mReviewViewModel = ViewModelProviders.of(getActivity()).get(MovieDetailsViewModel.class);
            mReviewViewModel.initReviews(mMovieId);
        }

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
        mReviewViewModel.getMovieReviews().observe(this, movieReviewResource -> {
            if (movieReviewResource != null) {
                switch (movieReviewResource.getStatus()) {
                    case SUCCESS:
                        Log.i(TAG, "data = " + movieReviewResource.getData().toString());
                        mReviewAdapter.setMoviesReviews(movieReviewResource.getData());
                        break;
                    case ERROR:
                        Toast.makeText(getContext(), getString(R.string.apiError),
                                Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        });
        return rootView;
    }
}
