package com.fachryar.moviecatalogue.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fachryar.moviecatalogue.R;
import com.fachryar.moviecatalogue.model.TvShow;
import com.fachryar.moviecatalogue.utils.AppConfig;
import com.fachryar.moviecatalogue.utils.DateFormat;

import java.util.ArrayList;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TvShowRecyclerAdapter extends RecyclerView.Adapter<TvShowRecyclerAdapter.TvShowViewHolder> {
    private ArrayList<TvShow> listTvShow;
    private OnItemClickCallback onItemClickCallback;
    private TvShow tvShow;

    public TvShowRecyclerAdapter() {
    }

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    public void setListTvShow(ArrayList<TvShow> tvShow){
        this.listTvShow = tvShow;
    }

    @NonNull
    @Override
    public TvShowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list, parent, false);
        return new TvShowViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final TvShowViewHolder holder, int position) {
        setRatingOverview(holder, position);
        insertValue(holder, position);
    }

    @Override
    public int getItemCount() {
        final int limit = 15;
        if (listTvShow.size() > limit){
            return limit;
        } else {
            return listTvShow.size();
        }
    }

    class TvShowViewHolder extends RecyclerView.ViewHolder {
        TextView textOverview, textDate, textTitle, textRatings;
        ImageView imgPoster;

        TvShowViewHolder(@NonNull View v) {
            super(v);

            imgPoster = v.findViewById(R.id.imgPoster);
            textTitle = v.findViewById(R.id.tvTitle);
            textDate = v.findViewById(R.id.tvRelease);
            textOverview = v.findViewById(R.id.tvOverview);
            textRatings = v.findViewById(R.id.tvRating);
        }
    }

    private void setRatingOverview(@NonNull final TvShowViewHolder holder, int position){
        tvShow = listTvShow.get(position);

        // Rating color
        if (tvShow.getRatings() >= 7.0){
            holder.textRatings.setBackgroundResource(R.drawable.ic_green);
        } else if (tvShow.getRatings() <= 4.0){
            holder.textRatings.setBackgroundResource(R.drawable.ic_red);
        } else {
            holder.textRatings.setBackgroundResource(R.drawable.ic_yellow);
        }

        if (tvShow.getOverview().isEmpty()){
            holder.textOverview.setText(holder.itemView.getContext().getString(R.string.overview_unavailable));
        } else {
            holder.textOverview.setText(tvShow.getOverview());
        }
    }

    private void insertValue(@NonNull final TvShowViewHolder holder, int position){
        tvShow = listTvShow.get(position);

        Glide.with(holder.itemView.getContext()).load(AppConfig.BASE_POSTER_URL + tvShow.getPoster())
                .into(holder.imgPoster);

        holder.textTitle.setText(tvShow.getShow_name());

        if (tvShow.getFirst_release() != null){
            holder.textDate.setText(holder.itemView.getContext()
                    .getString(R.string.kurung, DateFormat.getYear(tvShow.getFirst_release())));
        }

        holder.textRatings.setText(String.format(Locale.getDefault(),"%.0f%%", tvShow.getRatings()*10));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickCallback.onItemClicked(listTvShow.get(holder.getAdapterPosition()));
            }
        });
    }

    public interface OnItemClickCallback {
        void onItemClicked(TvShow data);
    }
}