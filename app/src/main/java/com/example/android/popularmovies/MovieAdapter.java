package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.popularmovies.Utils.Movie;
import com.example.android.popularmovies.Utils.MovieUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    /**
     *  Custom ViewHolder
     */
    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        ImageView mMoviePosterImageView;

        public MovieViewHolder(View itemView) {
            super(itemView);
            mMoviePosterImageView = itemView.findViewById(R.id.ivMoviePosterItem);
        }
    }


    private Context mContext;
    private List<Movie> mMoviesList;

    public MovieAdapter(Context context) {
        this.mContext = context;
    }

    public MovieAdapter(Context context, List<Movie> moviesList) {
        this.mContext = context;
        this.mMoviesList = moviesList;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflates the custom layout
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View rootView = inflater.inflate(R.layout.recyclerview_item, parent, false);

        // Return a new holder instance
        MovieViewHolder viewHolder = new MovieViewHolder(rootView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MovieViewHolder holder, int position) {
        // Get the movie at the given position
        final Movie movie = mMoviesList.get(position);

        // Get the path to the movies poster
        String posterUrl = MovieUtils.BASE_IMAGE_URL + MovieUtils.POSTER_IMAGE_SIZE_MAIN
                           + movie.getPosterThumbnail();

        // Set the image resource for the ImageView
        Picasso.get()
                .load(posterUrl)
                .into(holder.mMoviePosterImageView);

        // Set the onCLick method for the image so that it will launch the detail activity when
        // it is clicked on by the user.
        holder.mMoviePosterImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent detailIntent = new Intent(mContext, MovieDetails.class);
                detailIntent.putExtra("movie", movie);
                mContext.startActivity(detailIntent);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (mMoviesList == null) {
            return 0;
        }
        return this.mMoviesList.size();
    }

    public void setMoviesList(List<Movie> movieList) {
        this.mMoviesList = movieList;
        this.notifyDataSetChanged();
    }
}


