package com.example.android.popularmovies.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.model.MovieTrailer;

import java.util.List;

public class MovieTrailerAdapter extends RecyclerView.Adapter<MovieTrailerAdapter.MovieTrailerViewHolder> {

    private static final String TAG = "MovieTrailerAdapter";
    private final Context mContext;
    private List<MovieTrailer> movieTrailerList;
    private final TrailerClickListener mListener;

    public MovieTrailerAdapter(Context context, TrailerClickListener listener) {
        this.mContext = context;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public MovieTrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View rootView = inflater.inflate(R.layout.item_movie_trailer, parent, false);

        return new MovieTrailerViewHolder(rootView, mListener);

    }

    @Override
    public void onBindViewHolder(@NonNull MovieTrailerViewHolder movieTrailerViewHolder, int position) {
        // Display the title in the TextView
        movieTrailerViewHolder.mTrailerView.setText(movieTrailerList.get(position).getName());
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

    public MovieTrailer getItemAtPosition(int position) {
        if (movieTrailerList != null && position >= 0) {
            return movieTrailerList.get(position);
        }
        return null;
    }

    public static class MovieTrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        final TextView mTrailerView;
        final ImageButton mShareButton;
        private final TrailerClickListener mListener;

        MovieTrailerViewHolder(View itemView, TrailerClickListener listener) {
            super(itemView);
            mTrailerView = itemView.findViewById(R.id.trailerTextView);
            mShareButton = itemView.findViewById(R.id.trailer_share_button);
            this.mListener = listener;
            mTrailerView.setOnClickListener(this);
            mShareButton.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            Log.i(TAG,"" + view.getId());
            switch (view.getId()) {
                case R.id.trailerTextView:
                    mListener.onTrailerClicked(getAdapterPosition());
                    break;
                case R.id.trailer_share_button:
                    mListener.onShareTrailer(getAdapterPosition());
                    break;
                default:
                    break;
            }
        }
    }

}
