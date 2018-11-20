package com.example.android.popularmovies;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies.Utils.MovieUtils;
import com.example.android.popularmovies.ViewModels.DetailsViewModel;
import com.example.android.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;


public class MovieDetails extends AppCompatActivity {

    private static final String TAG = "MovieDetails";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        // Get references to the views displaying information
        TextView titleTv = (TextView) findViewById(R.id.titleTextView);
        TextView releaseDateTv = (TextView) findViewById(R.id.dateTextView);
        TextView ratingTv = (TextView) findViewById(R.id.ratingTextView);
        TextView descriptionTv = (TextView) findViewById(R.id.summaryTextView);

        ImageView posterIv = (ImageView) findViewById(R.id.detailPosterImageView);


        // Get the data passed in by the starting intent
        Movie movie = (Movie) getIntent().getParcelableExtra("movie");
        if (movie != null) {
            // Loads the poster image into the ImageView
            String posterUrl = MovieUtils.BASE_IMAGE_URL + MovieUtils.POSTER_IMAGE_SIZE_DETAIL
                    + movie.getPosterPath();
            Picasso.get()
                    .load(posterUrl)
                    .into(posterIv);

            // Sets the content to display in the relevant TextViews
            Log.i(TAG,movie.getReleaseDate());
            Log.i(TAG, movie.getPosterPath());
            titleTv.setText(movie.getTitle());
            releaseDateTv.setText(movie.getReleaseDate());
            ratingTv.setText(movie.getVoteAverage().toString());
            descriptionTv.setText(movie.getOverview());

            // Get the movies id
            int mMovieId = movie.getId();

            // Set up recyclerview and ViewModel
            setupRecyclerViewAndViewModel(mMovieId);
        } else {
            // Movie data unavailable
            finish();
            Toast.makeText(this, "There was an Error loading the data", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    private void setupRecyclerViewAndViewModel(int movieId) {

        // sets up the RecyclerView
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.trailerRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(MovieDetails.this);
        mRecyclerView.setLayoutManager(layoutManager);

        // Sets up ViewModel
        DetailsViewModel viewModel = ViewModelProviders.of(this).get(DetailsViewModel.class);
        viewModel.init(movieId);

        // Set up Observer for MovieTrailer information
        viewModel.getMovieTrailers().observe(this, movieTrailers -> {
            if (movieTrailers != null) {
                // Set up adapter with the data returned from the api request
                Log.i(TAG, "Sending trailers to the adapter");
                MovieTrailerAdapter adapter = new MovieTrailerAdapter(MovieDetails.this,
                        movieTrailers);
                mRecyclerView.setAdapter(adapter);
            }
        });

        // Set up Observer for MovieReview information
        viewModel.getMovieReviews().observe(this, movieReviews -> {
            if (movieReviews != null) {

            }
        });
    }
}
