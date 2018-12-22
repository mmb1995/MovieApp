package com.example.android.popularmovies;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies.Utils.MovieUtils;
import com.example.android.popularmovies.ViewModels.FactoryViewModel;
import com.example.android.popularmovies.ViewModels.MovieDetailsViewModel;
import com.example.android.popularmovies.adapter.MovieDetailsPageAdapter;
import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.views.CustomViewPager;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;


public class MovieDetails extends AppCompatActivity implements HasSupportFragmentInjector{
    private static final String TAG = "MovieDetails";
    private static final String BUNDLE_ID = "id";

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;
    @Inject
    FactoryViewModel mFactoryViewModel;
    MovieDetailsViewModel mDetailsViewModel;

    //ButterKnife
    @BindView(R.id.titleTextView) TextView mTitleView;
    @BindView(R.id.dateTextView) TextView mReleaseDateView;
    @BindView(R.id.ratingTextView) TextView mRatingView;
    @BindView(R.id.summaryTextView) TextView mSummaryView;
    @BindView(R.id.detailPosterImageView) ImageView mPosterImageView;
    @BindView(R.id.favorites_button)
    CheckBox mFavoritesButton;
    @BindView(R.id.view_pager)
    CustomViewPager mViewPager;
    @BindView(R.id.movieDetailsTabLayout) TabLayout mTabLayout;

    private Movie mMovie;
    private MovieDetailsPageAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);
        AndroidInjection.inject(this);

        // Get the data passed in by the starting intent
        mMovie = (Movie) getIntent().getParcelableExtra("movie");

        // load the movie data into the ui
        if (mMovie != null) {
            // Loads the poster image into the ImageView
            String posterUrl = MovieUtils.BASE_IMAGE_URL + MovieUtils.POSTER_IMAGE_SIZE_DETAIL
                    + mMovie.getPosterPath();
            Picasso.get()
                    .load(posterUrl)
                    .placeholder(R.drawable.loading_image)
                    .error(R.drawable.poster_error)
                    .into(mPosterImageView);

            // Sets the content to display in the relevant TextViews
            mTitleView.setText(mMovie.getTitle());
            mReleaseDateView.setText(mMovie.getReleaseDate());
            mRatingView.setText(mMovie.getVoteAverage().toString());
            mSummaryView.setText(mMovie.getOverview());
            mDetailsViewModel = ViewModelProviders.of(this, mFactoryViewModel)
                    .get(MovieDetailsViewModel.class);

            setUpFavoritesButton();

            // Set up ViewPager and connect it to the TabLayout
            mAdapter = new MovieDetailsPageAdapter(getSupportFragmentManager(), mMovie.getId(), this);
            mViewPager.setAdapter(mAdapter);
            mTabLayout.setupWithViewPager(mViewPager);
        } else {
            // End activity if movie data is unavailable
            finish();
        }
    }

    @Override
    public DispatchingAndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }

    private void setUpFavoritesButton() {
        Log.i(TAG, "Favorite = " + mMovie.isFavorite);
        if (mMovie.isFavorite == 1) {
            mFavoritesButton.setChecked(true);
        } else {
            mFavoritesButton.setChecked(false);
        }

        // Set up listener for changes in toggle button state
        mFavoritesButton.setOnCheckedChangeListener((favoritesButton, isChecked) -> {
            if (isChecked) {
                addToFavorites();
            } else {
                removeFavorite();
            }
        });
    }

    /**
     * Adds the movie to the favorites database
     */
    private void addToFavorites() {
        Toast.makeText(MovieDetails.this, "Movie marked as favorite",
                Toast.LENGTH_SHORT).show();
        mMovie.setAsFavorite();
        mDetailsViewModel.addFavorite(mMovie);
    }

    /**
     * Removes movie from the favorites database
     */
    private void removeFavorite() {
        Toast.makeText(this, "Movie removed from favorites",
                Toast.LENGTH_SHORT).show();
        mMovie.unmarkFavorite();
        mDetailsViewModel.removeFavorite(mMovie);

    }


}
