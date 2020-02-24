package com.dicoding.fachry.favoriteapp;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = AppConfig.FAVORITE_TABLE)
public class Favorite implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int movieId;
    private String title;
    private String type;
    private String overview;
    private String poster;
    private String backdrop;
    private Double rating;

    public Favorite() {
    }

    public Favorite(String title) {
        this.title = title;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Double getRating() {
        return rating;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getBackdrop() {
        return backdrop;
    }

    public void setBackdrop(String backdrop) {
        this.backdrop = backdrop;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(movieId);
        dest.writeString(title);
        dest.writeString(type);
        dest.writeString(overview);
        dest.writeString(poster);
        dest.writeString(backdrop);
        dest.writeDouble(rating);
    }

    protected Favorite(Parcel in) {
        id = in.readInt();
        movieId = in.readInt();
        title = in.readString();
        type = in.readString();
        overview = in.readString();
        poster = in.readString();
        backdrop = in.readString();
        rating = in.readDouble();
    }

    public static final Creator<Favorite> CREATOR = new Creator<Favorite>() {
        @Override
        public Favorite createFromParcel(Parcel in) {
            return new Favorite(in);
        }

        @Override
        public Favorite[] newArray(int size) {
            return new Favorite[size];
        }
    };
}
