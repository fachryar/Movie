package com.fachryar.moviecatalogue.adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.fachryar.moviecatalogue.view.FavoriteActivity;
import com.fachryar.moviecatalogue.viewmodel.FavoriteViewModel;
import com.fachryar.moviecatalogue.R;
import com.fachryar.moviecatalogue.room.Favorite;
import com.fachryar.moviecatalogue.utils.AppConfig;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

public class FavoriteRecyclerAdapter extends RecyclerView.Adapter<FavoriteRecyclerAdapter.FavoriteHolder> {
    private List<Favorite> listFavorite = new ArrayList<>();
    private OnItemClickCallback onItemClickCallback;
    private FavoriteViewModel favoriteViewModel;


    public FavoriteRecyclerAdapter(){
    }

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    public void setFavorites(List<Favorite> favorites){
        this.listFavorite = favorites;
    }

    @NonNull
    @Override
    public FavoriteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorite_list, parent,false);

        return new FavoriteHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final FavoriteHolder holder, final int position) {
        Favorite favorite = listFavorite.get(position);

        favoriteViewModel = ViewModelProviders.of((FragmentActivity) holder.itemView.getContext()).get(FavoriteViewModel.class);
        holder.textTitle.setText(holder.itemView.getResources().getString(R.string.title_detail, favorite.getTitle(), favorite.getType()));

        Glide.with(holder.itemView.getContext()).load(AppConfig.BASE_POSTER_URL + favorite.getPoster())
                .into(holder.imagePoster);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickCallback.onItemClicked(listFavorite.get(holder.getAdapterPosition()));
            }
        });

        holder.btnUnfav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlerDialog(holder, position);
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return listFavorite.size();
    }


    class FavoriteHolder extends RecyclerView.ViewHolder{
        private TextView textTitle;
        private ImageButton btnUnfav;
        private ImageView imagePoster;


        public FavoriteHolder(@NonNull View itemView) {
            super(itemView);

            textTitle = itemView.findViewById(R.id.tvTitleFav);
            btnUnfav = itemView.findViewById(R.id.btnUnfav);
            imagePoster = itemView.findViewById(R.id.imgPosterFav);
        }
    }

    private void showAlerDialog(@NonNull final FavoriteHolder holder, final int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
        builder.setTitle(holder.itemView.getResources().getString(R.string.remove_favorite));
        builder.setCancelable(false)
                .setPositiveButton(holder.itemView.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Favorite favorite = listFavorite.get(position);
                        favoriteViewModel.delete(favorite);
                        Toast.makeText(holder.itemView.getContext(), holder.itemView.getResources().getString(R.string.successfully_remove_fav, favorite.getTitle()), Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(holder.itemView.getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public interface OnItemClickCallback {
        void onItemClicked(Favorite data);
    }
}
