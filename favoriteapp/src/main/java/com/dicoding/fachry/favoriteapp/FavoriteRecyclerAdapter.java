package com.dicoding.fachry.favoriteapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FavoriteRecyclerAdapter extends RecyclerView.Adapter<FavoriteRecyclerAdapter.FavoriteViewHolder> {
    private List<Favorite> favoriteList;
    private Favorite favorite;

    public FavoriteRecyclerAdapter(){}

    public void setListFavorites(List<Favorite> favorites) {this.favoriteList = favorites;}

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorite_list, parent, false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteRecyclerAdapter.FavoriteViewHolder holder, int position) {
        favorite = favoriteList.get(position);

        Glide.with(holder.itemView.getContext()).load(AppConfig.BASE_POSTER_URL + favorite.getPoster())
                .into(holder.imagePoster);
        holder.tvTitle.setText(favorite.getTitle());
    }

    @Override
    public int getItemCount() {
        return favoriteList.size();
    }

    class FavoriteViewHolder extends RecyclerView.ViewHolder {
        ImageView imagePoster;
        TextView tvTitle;

        FavoriteViewHolder(@NonNull View v) {
            super(v);

            imagePoster = v.findViewById(R.id.imgPoster);
            tvTitle = v.findViewById(R.id.tvTitle);
        }
    }
}
