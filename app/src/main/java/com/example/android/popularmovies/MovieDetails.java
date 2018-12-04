package com.example.android.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.Utils.MovieUtils;
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

        // load the movie data into the ui
        if (movie != null) {
            // Loads the poster image into the ImageView
            String posterUrl = MovieUtils.BASE_IMAGE_URL + MovieUtils.POSTER_IMAGE_SIZE_DETAIL
                    + movie.getPosterPath();
            Picasso.get()
                    .load(posterUrl)
                    .placeholder(R.drawable.loading_image)
                    .error(R.drawable.poster_error)
                    .into(posterIv);

            // Sets the content to display in the relevant TextViews
            Log.i(TAG,movie.getReleaseDate());
            Log.i(TAG, movie.getPosterPath());
            titleTv.setText(movie.getTitle());
            releaseDateTv.setText(movie.getReleaseDate());
            ratingTv.setText(movie.getVoteAverage().toString());
            descriptionTv.setText(movie.getOverview());
        } else {
            // End activity if movie data is unavailable
            finish();
        }
    }


}
