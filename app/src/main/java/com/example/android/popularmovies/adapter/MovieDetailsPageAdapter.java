package com.example.android.popularmovies.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.fragment.TrailerFragment;

public class MovieDetailsPageAdapter extends FragmentPagerAdapter {
    private static final int NUM_ITEMS = 2;
    private static final String ID_KEY = "id";
    private int mMovieId;
    private Context mContext;

    public MovieDetailsPageAdapter(FragmentManager fragmentManager, int movieId, Context context) {
        super(fragmentManager);
        mMovieId = movieId;
        this.mContext = context;
    }


    @Override
    public Fragment getItem(int position) {
        // Set up the bundle to pass the movieId to the new fragment
        Fragment fragment;
        Bundle mBundle = new Bundle();
        mBundle.putInt(ID_KEY, mMovieId);
        switch (position) {
            case 0:
                fragment = new TrailerFragment();
                break;
            case 1:
                fragment = new TrailerFragment();
                break;
            default:
                fragment = new TrailerFragment();
        }
        fragment.setArguments(mBundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }


    /**
     * Determines the titles that will appear on the tabs
     * @param position
     * @return
     */
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return mContext.getString(R.string.trailer_tab);
            case 1:
                return mContext.getString(R.string.reviews_tab);
            default:
                return null;
        }
    }
}
