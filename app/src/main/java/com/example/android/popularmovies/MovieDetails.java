package com.example.android.popularmovies;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies.Utils.MovieUtils;
import com.example.android.popularmovies.ViewModels.FactoryViewModel;
import com.example.android.popularmovies.ViewModels.MovieDetailsViewModel;
import com.example.android.popularmovies.adapter.MovieDetailsPageAdapter;
import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.views.CustomViewPager;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;


public class MovieDetails extends AppCompatActivity implements HasSupportFragmentInjector{
    private static final String TAG = "MovieDetails";
    public static final String BUNDLE_ID = "movieId";

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;
    @Inject
    FactoryViewModel mFactoryViewModel;

    //ButterKnife
    @BindView(R.id.titleTextView) TextView mTitleView;
    @BindView(R.id.dateTextView) TextView mReleaseDateView;
    @BindView(R.id.ratingTextView) TextView mRatingView;
    @BindView(R.id.summaryTextView) TextView mSummaryView;
    @BindView(R.id.runtimeTextView) TextView mRuntimeView;
    @BindView(R.id.detailPosterImageView) ImageView mPosterImageView;
    @BindView(R.id.detailPosterProgressBar)
    ProgressBar mProgressBar;
    @BindView(R.id.favorites_button)
    CheckBox mFavoritesButton;
    @BindView(R.id.view_pager)
    CustomViewPager mViewPager;
    @BindView(R.id.movieDetailsTabLayout) TabLayout mTabLayout;
    @BindView(R.id.toolbar)
    android.support.v7.widget.Toolbar mToolbar;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingToolBar;
    @BindView(R.id.app_bar_layout)
    AppBarLayout mAppBar;

    private Movie mMovie;
    private MovieDetailsViewModel mDetailsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);

        // Only show title when toolbar is collapsed
        mAppBar.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            boolean isShown = true;
            int scrollRange = -1;

            if(scrollRange == -1) {
                scrollRange = appBarLayout.getTotalScrollRange();
            }
            if (scrollRange + verticalOffset == 0) {
                if (mMovie != null) {
                    mCollapsingToolBar.setTitle(mMovie.getTitle());
                } else {
                    mCollapsingToolBar.setTitle(" ");
                }
                isShown = true;
            } else if (isShown) {
                mCollapsingToolBar.setTitle(" ");
                isShown= false;
            }
        });
        AndroidInjection.inject(this);
        getSelectedMovie();
    }

    @Override
    public DispatchingAndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }

    private void getSelectedMovie() {
        this.mDetailsViewModel = ViewModelProviders.of(this, mFactoryViewModel)
                .get(MovieDetailsViewModel.class);
        mDetailsViewModel.init(getIntent().getIntExtra(BUNDLE_ID, 0));
        mDetailsViewModel.getMovie().observe(this, response -> {
            if (response != null) {
                switch (response.status) {
                    case SUCCESS:
                        this.mMovie = (Movie) response.data;
                        configureViews();
                        setUpViewPager();
                        break;
                    case ERROR:
                        Toast.makeText(this, getString(R.string.apiError), Toast.LENGTH_SHORT).show();
                        finish();
                    default:
                        break;
                }
            }
        });
    }

    private void configureViews() {
        String posterUrl = MovieUtils.BASE_IMAGE_URL + MovieUtils.POSTER_IMAGE_SIZE_DETAIL
                + mMovie.getPosterPath();

        Picasso.get()
                .load(posterUrl)
                .into(mPosterImageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        mProgressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Exception e) {
                        mProgressBar.setVisibility(View.GONE);
                    }
                });

        mTitleView.setText(mMovie.getTitle());

        // Parses the date to get the release year
        String releaseDate = mMovie.getReleaseDate();
        String releaseYear;
        if (releaseDate != null && releaseDate.length() >= 4) {
            releaseYear = releaseDate.substring(0, 4);
        } else {
            releaseYear = "N/A";
        }
        mReleaseDateView.setText(releaseYear);
        mRatingView.setText(mMovie.getVoteAverage().toString());
        mSummaryView.setText(mMovie.getOverview());
        setRuntime();
        setUpFavoritesButton();
    }

    /**
     * Formats the runtime returned by the ViewModel
     */
    private void setRuntime() {
        Integer runtime = mMovie.getRuntime();
        if (runtime != null) {
            int hours = runtime / 60;
            int minutes = runtime % 60;
            if (hours > 0) {
                String formattedRuntime = hours + "h " + minutes + "min";
                mRuntimeView.setText(formattedRuntime);
            } else {
                String formattedRuntime = minutes + "min";
                mRuntimeView.setText(formattedRuntime);
            }
        } else {
            mRuntimeView.setText("N/A");
        }
    }

    private void setUpViewPager() {
        MovieDetailsPageAdapter mAdapter = new MovieDetailsPageAdapter(getSupportFragmentManager(), mMovie.getId(), this);
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
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
