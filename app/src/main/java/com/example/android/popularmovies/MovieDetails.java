package com.example.android.popularmovies;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.Utils.MovieUtils;
import com.example.android.popularmovies.adapter.MovieDetailsPageAdapter;
import com.example.android.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MovieDetails extends AppCompatActivity {
    private static final String TAG = "MovieDetails";
    private static final String BUNDLE_ID = "id";

    //ButterKnife
    @BindView(R.id.titleTextView) TextView mTitleView;
    @BindView(R.id.dateTextView) TextView mReleaseDateView;
    @BindView(R.id.ratingTextView) TextView mRatingView;
    @BindView(R.id.summaryTextView) TextView mSummaryView;
    @BindView(R.id.detailPosterImageView) ImageView mPosterImageView;
    @BindView(R.id.view_pager) ViewPager mViewPager;
    @BindView(R.id.movieDetailsTabLayout) TabLayout mTabLayout;

    private int mMovieId;
    private MovieDetailsPageAdapter mAdapter;

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

            // Set up ViewPager and connect it to the TabLayout
            mAdapter = new MovieDetailsPageAdapter(getSupportFragmentManager(), mMovieId, this);
            mViewPager.setAdapter(mAdapter);
            mTabLayout.setupWithViewPager(mViewPager);
            //mViewPager.setCurrentItem(0);
        } else {
            // End activity if movie data is unavailable
            finish();
        }
    }


}
