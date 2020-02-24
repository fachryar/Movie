package com.fachryar.moviecatalogue.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TvResult implements Parcelable {
    @SerializedName("page")
    private int page;

    @SerializedName("total_pages")
    private int total_pages;


    @SerializedName("results")
    private ArrayList<TvShow> listTv;

    public int getPage() {
        return page;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public ArrayList<TvShow> getTvResults() {
        return listTv;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.listTv);
    }

    protected TvResult(Parcel in){
        this.listTv = in.createTypedArrayList(TvShow.CREATOR);
    }

    public TvResult() {
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