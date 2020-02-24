package com.fachryar.moviecatalogue.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Result implements Parcelable {

    @SerializedName("page")
    private int page;

    @SerializedName("total_pages")
    private int total_pages;

    @SerializedName("results")
    private ArrayList<Movies> listMovies;

    public int getPage() {
        return page;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public ArrayList<Movies> getMovieResults(){
        return listMovies;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.listMovies);
    }

    protected Result(Parcel in){
        this.listMovies = in.createTypedArrayList(Movies.CREATOR);
    }

    public Result() {
    }

    public static final Creator<Result> CREATOR = new Creator<Result>() {
        @Override
        public Result createFromParcel(Parcel in) {
            return new Result(in);
        }

        @Override
        public Result[] newArray(int size) {
            return new Result[size];
        }
    };
}
