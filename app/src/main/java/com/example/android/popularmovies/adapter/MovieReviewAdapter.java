package com.example.android.popularmovies.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.model.MovieReview;
import com.ms.square.android.expandabletextview.ExpandableTextView;

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
        movieReviewViewHolder.mExpandableTextView.setText(movieReviewList.get(position).getContent());
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

    public MovieReview getItemAtPosition(int position) {
        if (movieReviewList != null && position >= 0) {
            return movieReviewList.get(position);
        }
        return null;
    }


    public static class MovieReviewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView mAuthorTextView;
        final ExpandableTextView mExpandableTextView;

        MovieReviewViewHolder(View itemView) {
            super(itemView);
            mAuthorTextView = itemView.findViewById(R.id.author_text_view);
            mExpandableTextView= itemView.findViewById(R.id.expand_text_view);
        }

        @Override
        public void onClick(View view) {
            if (((TextView) view).getLineCount() == 5) {
                ((TextView) view).setMaxLines(Integer.MAX_VALUE);
                ((TextView) view).setEllipsize(null);
            } else if (((TextView) view).getLineCount() > 5) {
                ((TextView) view).setMaxLines(5);
                ((TextView) view).setEllipsize(TextUtils.TruncateAt.END);
            }
        }
    }
}
