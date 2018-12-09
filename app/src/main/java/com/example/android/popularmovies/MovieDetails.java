package com.example.android.popularmovies;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.Utils.MovieUtils;
import com.example.android.popularmovies.fragment.TrailerFragment;
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
    @BindView(R.id.fragment_container) FrameLayout mFragmentContainer;
    @BindView(R.id.movieDetailsTabLayout) TabLayout mTabLayout;

    private int mMovieId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);

        // Get the data passed in by the starting intent
        Movie movie = (Movie) getIntent().getParcelableExtra("movie");

        // Set up tab layout
        mTabLayout.addTab(mTabLayout.newTab().setText(getString(R.string.trailer_tab)));
        mTabLayout.addTab(mTabLayout.newTab().setText(getString(R.string.reviews_tab)));

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
            setupTabLayout();
        } else {
            // End activity if movie data is unavailable
            finish();
        }
    }

    private void setupTabLayout() {
        mTabLayout.addOnTabSelectedListener( new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        Bundle trailerBundle = new Bundle();
                        trailerBundle.putInt(BUNDLE_ID, mMovieId);
                        TrailerFragment trailerFrag = new TrailerFragment();
                        trailerFrag.setArguments(trailerBundle);
                        updateFragments(trailerFrag);
                    case 1:
                        //TODO add review fragment
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        // TODO: remove this and add viewpager
        TabLayout.Tab tab = mTabLayout.getTabAt(0);
        tab.select();
    }

    private void updateFragments(Fragment fragment) {
        FragmentManager fragManager = getSupportFragmentManager();
        fragManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

}
