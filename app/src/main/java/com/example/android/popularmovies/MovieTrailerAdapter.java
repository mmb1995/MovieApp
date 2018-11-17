package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.popularmovies.Utils.MovieUtils;
import com.example.android.popularmovies.model.MovieTrailer;

import java.util.List;

public class MovieTrailerAdapter extends RecyclerView.Adapter<MovieTrailerAdapter.MovieTrailerViewHolder> {

    private static final String TAG = "MovieTrailerAdapter";
    private final Context mContext;
    private List<MovieTrailer> movieTrailerList;

    public MovieTrailerAdapter(Context context, List<MovieTrailer> trailerList) {
        this.mContext = context;
        this.movieTrailerList = trailerList;
    }

    @NonNull
    @Override
    public MovieTrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View rootView = inflater.inflate(R.layout.movie_trailer_recyclerview_item, parent, false);

        return new MovieTrailerViewHolder(rootView);

    }

    @Override
    public void onBindViewHolder(@NonNull MovieTrailerViewHolder movieTrailerViewHolder, int position) {
        final MovieTrailer mMovieTrailer = movieTrailerList.get(position);

        String title = mMovieTrailer.getName();

        // Display the title in the TextView
        movieTrailerViewHolder.mTrailerView.setText(title);

        // Sets the url for the trailer
        final String trailerUrl = MovieUtils.BASE_YOUTUBE_URL + mMovieTrailer.getKey();

        // Sets the onClick listener to play the movie trailer
        movieTrailerViewHolder.mTrailerView.setOnClickListener(view -> {
            // implicit intent to use apps native video player to play the given trailer
            Log.i(TAG, "Starting intent to play video");
            Intent playTrailerIntent = new Intent(Intent.ACTION_VIEW);
            playTrailerIntent.setData(Uri.parse(trailerUrl));
            Log.i(TAG, "trailer url = " + trailerUrl);
            mContext.startActivity(Intent.createChooser(playTrailerIntent, "Complete action using"));

        });
    }

    @Override
    public int getItemCount() {
        if (this.movieTrailerList == null) {
            return 0;
        }
        return this.movieTrailerList.size();
    }

    public void setMoviesList(List<MovieTrailer> trailerList) {
        this.movieTrailerList = trailerList;
        this.notifyDataSetChanged();
    }

    public static class MovieTrailerViewHolder extends RecyclerView.ViewHolder{
        final TextView mTrailerView;

        MovieTrailerViewHolder(View itemView) {
            super(itemView);
            mTrailerView = itemView.findViewById(R.id.trailerTextView);
        }

    }
}
