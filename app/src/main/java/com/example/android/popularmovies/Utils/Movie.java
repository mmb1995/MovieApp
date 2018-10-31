package com.example.android.popularmovies.Utils;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {
    private String title;
    private String description;
    private String posterThumbnail;
    private String rating;
    private String releaseDate;

    public Movie() {}

    public Movie(String title, String description, String posterThumbnail, String rating, String releaseDate) {
        this.title = title;
        this.description = description;
        this.posterThumbnail = posterThumbnail;
        this.rating = rating;
        this.releaseDate = releaseDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterThumbnail() {
        return posterThumbnail;
    }

    public void setPosterThumbnail(String posterThumbnail) {
        this.posterThumbnail = posterThumbnail;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    /**
     * Retrieving Movie data from Parcel object
     * This constructor is invoked by the method createFromParcel(Parcel source)
     * of the object Creator
     */
    private Movie(Parcel in) {
        this.title = in.readString();
        this.description = in.readString();
        this.posterThumbnail = in.readString();
        this.rating = in.readString();
        this.releaseDate = in.readString();
    }

    /**
     * These method overrides the methods specified in the Parcelable interface
     */
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(posterThumbnail);
        dest.writeString(rating);
        dest.writeString(releaseDate);
    }

    /**
     * Implements the Parcelable Creator in order to be able to pass the Movie object
     * between activities
     */
    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
