package com.fachryar.moviecatalogue.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fachryar.moviecatalogue.utils.AppConfig;
import com.fachryar.moviecatalogue.utils.DateFormat;
import com.fachryar.moviecatalogue.model.Movies;
import com.fachryar.moviecatalogue.R;

import java.util.ArrayList;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MovieRecyclerAdapter extends RecyclerView.Adapter<MovieRecyclerAdapter.MoviesViewHolder> {
    private ArrayList<Movies> listMovies;
    private OnItemClickCallback onItemClickCallback;
    private Movies movie;

    public MovieRecyclerAdapter(){
    }

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    public void setListMovies(ArrayList<Movies> movies){
        this.listMovies = movies;
    }

    @NonNull
    @Override
    public MoviesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list, parent, false);
        return new MoviesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MoviesViewHolder holder, int position) {
        setRatingOverview(holder, position);
        insertValue(holder, position);
    }

    @Override
    public int getItemCount() {
        final int limit = 15;
        if (listMovies.size() > limit){
            return limit;
        } else {
            return listMovies.size();
        }
    }

    class MoviesViewHolder extends RecyclerView.ViewHolder {
        TextView textTitle, textOverview, textRatings, textDate;
        ImageView imgPoster;

        MoviesViewHolder(@NonNull View v) {
            super(v);

            imgPoster = v.findViewById(R.id.imgPoster);
            textTitle = v.findViewById(R.id.tvTitle);
            textOverview = v.findViewById(R.id.tvOverview);
            textRatings = v.findViewById(R.id.tvRating);
            textDate = v.findViewById(R.id.tvRelease);
        }
    }

    private void setRatingOverview(@NonNull final MoviesViewHolder holder, int position){
        movie = listMovies.get(position);

        // Rating color
        if (movie.getRatings() >= 7.0){
            holder.textRatings.setBackgroundResource(R.drawable.ic_green);
        } else if (movie.getRatings() <= 4.0){
            holder.textRatings.setBackgroundResource(R.drawable.ic_red);
        } else {
            holder.textRatings.setBackgroundResource(R.drawable.ic_yellow);
        }

        if (movie.getOverview().isEmpty()){
            holder.textOverview.setText(holder.itemView.getContext().getString(R.string.overview_unavailable));
        } else {
            holder.textOverview.setText(movie.getOverview());
        }
    }

    private void insertValue(@NonNull final MoviesViewHolder holder, int position){
        movie = listMovies.get(position);

        Glide.with(holder.itemView.getContext()).load(AppConfig.BASE_POSTER_URL + movie.getPoster())
                .into(holder.imgPoster);

        holder.textRatings.setText(String.format(Locale.getDefault(),"%.0f%%", movie.getRatings()*10));
        holder.textTitle.setText(movie.getTitle());
        holder.textDate.setText(holder.itemView.getContext()
                .getString(R.string.kurung, DateFormat.getYear(movie.getRelease_date())));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickCallback.onItemClicked(listMovies.get(holder.getAdapterPosition()));
            }
        });
    }

    public interface OnItemClickCallback {
        void onItemClicked(Movies data);
    }
}
