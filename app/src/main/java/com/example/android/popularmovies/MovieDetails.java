package com.example.android.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies.Utils.Movie;
import com.example.android.popularmovies.Utils.MovieUtils;
import com.squareup.picasso.Picasso;


public class MovieDetails extends AppCompatActivity {

    public static final String TAG = "MovieDetails";

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
                    + movie.getPosterThumbnail();
            Picasso.get()
                    .load(posterUrl)
                    .into(posterIv);

            // Sets the content to display in the relevant TextViews
            Log.i(TAG,movie.getReleaseDate());
            Log.i(TAG, movie.getPosterThumbnail());
            titleTv.setText(movie.getTitle());
            releaseDateTv.setText(movie.getReleaseDate());
            ratingTv.setText(movie.getRating());
            descriptionTv.setText(movie.getDescription());
        } else {
            // Movie data unavailable
            finish();
            Toast.makeText(this, "There was an Error loading the data", Toast.LENGTH_SHORT).show();
            return;
        }
    }
}
