package com.example.android.popularmovies.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.Utils.MovieUtils;
import com.example.android.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private final Context mContext;
    private List<Movie> mMoviesList;
    private RecyclerViewClickListener mListener;

    public MovieAdapter(Context context, RecyclerViewClickListener listener) {
        this.mContext = context;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflates the custom layout
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View rootView = inflater.inflate(R.layout.recyclerview_item, parent, false);

        // Return a new holder instance
        return new MovieViewHolder(rootView, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final MovieViewHolder holder, int position) {
        // Get the movie at the given position
        final Movie movie = mMoviesList.get(position);

        // Get the path to the movies poster
        String posterUrl = MovieUtils.BASE_IMAGE_URL + MovieUtils.POSTER_IMAGE_SIZE_MAIN
                           + movie.getPosterPath();

        // Set the image resource for the ImageView
        Picasso.get()
                .load(posterUrl)
                .placeholder(R.drawable.loading_image)
                .error(R.drawable.poster_error)
                .into(holder.mMoviePosterImageView);

    }

    @Override
    public int getItemCount() {
        if (mMoviesList == null) {
            return 0;
        }
        return this.mMoviesList.size();
    }

    /**
     * Returns the movie at the given position
     * @param position
     * @return
     */
    public Movie getItemAtPosition(int position) {
        if (mMoviesList != null && position >= 0) {
            return mMoviesList.get(position);
        }
        return null;
    }

    public void setMoviesList(List<Movie> movieList) {
        this.mMoviesList = movieList;
        this.notifyDataSetChanged();
    }

    /**
     *  Custom ViewHolder
     */
    public static class MovieViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener {
        final ImageView mMoviePosterImageView;
        private final RecyclerViewClickListener mListener;


        MovieViewHolder(View itemView, RecyclerViewClickListener listener) {
            super(itemView);
            mMoviePosterImageView = itemView.findViewById(R.id.ivMoviePosterItem);
            mListener = listener;
            itemView.setOnClickListener(this);
        }

        public void onClick(View view) {
            mListener.onClick(getAdapterPosition());
        }
    }
}


