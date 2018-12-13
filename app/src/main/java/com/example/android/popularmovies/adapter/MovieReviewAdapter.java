package com.example.android.popularmovies.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.model.MovieReview;

import java.util.List;

public class MovieReviewAdapter extends RecyclerView.Adapter<MovieReviewAdapter.MovieReviewViewHolder> {

    private static final String TAG = "MovieReviewAdapter";
    private final Context mContext;
    private List<MovieReview> movieReviewList;

    public MovieReviewAdapter(Context context) {
        this.mContext = context;
    }

    @NonNull
    @Override
    public MovieReviewAdapter.MovieReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View rootView = inflater.inflate(R.layout.item_movie_review, parent, false);

        return new MovieReviewAdapter.MovieReviewViewHolder(rootView);

    }

    @Override
    public void onBindViewHolder(@NonNull MovieReviewAdapter.MovieReviewViewHolder movieReviewViewHolder, int position) {
        // Display the title in the TextView
        movieReviewViewHolder.mAuthorTextView.setText(movieReviewList.get(position).getAuthor());
        movieReviewViewHolder.mContentTextView.setText(movieReviewList.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        if (this.movieReviewList == null) {
            return 0;
        }
        return this.movieReviewList.size();
    }

    public void setMoviesReviews(List<MovieReview> reviewList) {
        this.movieReviewList = reviewList;
        this.notifyDataSetChanged();
    }


    public static class MovieReviewViewHolder extends RecyclerView.ViewHolder {
        final TextView mAuthorTextView;
        final TextView mContentTextView;

        MovieReviewViewHolder(View itemView) {
            super(itemView);
            mAuthorTextView = itemView.findViewById(R.id.author_text_view);
            mContentTextView = itemView.findViewById(R.id.review_content_text_view);
        }

    }
}
