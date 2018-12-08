package com.example.android.popularmovies;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies.Utils.MovieUtils;
import com.example.android.popularmovies.ViewModels.MovieDetailsViewModel;
import com.example.android.popularmovies.adapter.MovieTrailerAdapter;
import com.example.android.popularmovies.adapter.RecyclerViewClickListener;
import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.model.MovieTrailer;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MovieDetails extends AppCompatActivity implements RecyclerViewClickListener {
    private static final String TAG = "MovieDetails";

    //ButterKnife
    @BindView(R.id.titleTextView) TextView mTitleView;
    @BindView(R.id.dateTextView) TextView mReleaseDateView;
    @BindView(R.id.ratingTextView) TextView mRatingView;
    @BindView(R.id.summaryTextView) TextView mSummaryView;
    @BindView(R.id.detailPosterImageView) ImageView mPosterImageView;
    @BindView(R.id.trailerRecyclerView) RecyclerView mTrailerRecyclerView;

    private int mMovieId;
    private MovieDetailsViewModel mMovieDetailsViewModel;
    private MovieTrailerAdapter mTrailerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);

        // Get the data passed in by the starting intent
        Movie movie = (Movie) getIntent().getParcelableExtra("movie");

        // load the movie data into the ui
        if (movie != null) {
            mMovieId = movie.getId();

            // Loads the poster image into the ImageView
            String posterUrl = MovieUtils.BASE_IMAGE_URL + MovieUtils.POSTER_IMAGE_SIZE_DETAIL
                    + movie.getPosterPath();
            Picasso.get()
                    .load(posterUrl)
                    .placeholder(R.drawable.loading_image)
                    .error(R.drawable.poster_error)
                    .into(mPosterImageView);

            // Sets the content to display in the relevant TextViews
            Log.i(TAG,movie.getReleaseDate());
            Log.i(TAG, movie.getPosterPath());
            mTitleView.setText(movie.getTitle());
            mReleaseDateView.setText(movie.getReleaseDate());
            mRatingView.setText(movie.getVoteAverage().toString());
            mSummaryView.setText(movie.getOverview());

            // Set up RecyclerView for movie trailers
            mTrailerRecyclerView.setLayoutManager(new LinearLayoutManager(MovieDetails.this));

            // Initialize ViewModel
            mMovieDetailsViewModel = ViewModelProviders.of(this).get(MovieDetailsViewModel.class);

            setUpTrailers();
        } else {
            // End activity if movie data is unavailable
            finish();
        }
    }

    /**
     * Gets the MovieTrailer data from the ViewModel and displays it
     */
    private void setUpTrailers() {
        mMovieDetailsViewModel.init(this.mMovieId);

        // Set up Observer for MovieTrailer information
        mMovieDetailsViewModel.getMovieTrailers().observe(this, movieTrailerResource -> {
            if (movieTrailerResource != null) {
                switch (movieTrailerResource.getStatus()) {
                    case SUCCESS:
                        // Set up adapter with the returned data
                        Log.i(TAG, "Sending trailers to adapter");
                        mTrailerAdapter = new MovieTrailerAdapter(MovieDetails.this,
                                movieTrailerResource.getData(), MovieDetails.this);
                        mTrailerRecyclerView.setAdapter(mTrailerAdapter);
                        break;
                    case ERROR:
                        Toast.makeText(MovieDetails.this, getString(R.string.apiError),
                                Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        });
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
}
