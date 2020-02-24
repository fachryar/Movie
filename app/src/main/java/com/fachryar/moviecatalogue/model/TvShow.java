package com.fachryar.moviecatalogue.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TvShow implements Parcelable {

    @SerializedName("name")
    private String show_name;

    @SerializedName("id")
    private int id;

    @SerializedName("poster_path")
    private String poster;

    @SerializedName("backdrop_path")
    private String backdrop;

    @SerializedName("overview")
    private String overview;

    @SerializedName("vote_average")
    private Double ratings;

    @SerializedName("first_air_date")
    private String first_release;

    @SerializedName("status")
    private String status;

    @SerializedName("number_of_episodes")
    private int episode;

    @SerializedName("number_of_seasons")
    private int season;

    @SerializedName("genres")
    private ArrayList<Genre> listGenre;

    public TvShow() {
    }

    public String getShow_name() {
        return show_name;
    }

    public void setShow_name(String show_name) {
        this.show_name = show_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public Double getRatings() {
        return ratings;
    }

    public void setRatings(Double ratings) {
        this.ratings = ratings;
    }

    public String getFirst_release() {
        return first_release;
    }

    public void setFirst_release(String first_release) {
        this.first_release = first_release;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getEpisode() {
        return episode;
    }

    public void setEpisode(int episode) {
        this.episode = episode;
    }

    public int getSeason() {
        return season;
    }

    public void setSeason(int season) {
        this.season = season;
    }

    public ArrayList<Genre> getListGenre() {
        return listGenre;
    }

    public void setListGenre(ArrayList<Genre> listGenre) {
        this.listGenre = listGenre;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    protected TvShow(Parcel in) {
        poster = in.readString();
        backdrop = in.readString();
        show_name = in.readString();
        overview = in.readString();
        id = in.readInt();
        ratings = in.readDouble();
        first_release = in.readString();
        status = in.readString();
        episode = in.readInt();
        season = in.readInt();
        listGenre = in.createTypedArrayList(Genre.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(poster);
        dest.writeString(backdrop);
        dest.writeString(show_name);
        dest.writeString(overview);
        dest.writeInt(id);
        dest.writeDouble(ratings);
        dest.writeString(first_release);
        dest.writeString(status);
        dest.writeInt(episode);
        dest.writeInt(season);
        dest.writeTypedList(listGenre);
    }

    public static final Creator<TvShow> CREATOR = new Creator<TvShow>() {
        @Override
        public TvShow createFromParcel(Parcel in) {
            return new TvShow(in);
        }

        @Override
        public TvShow[] newArray(int size) {
            return new TvShow[size];
        }
    };
}
