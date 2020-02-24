package com.fachryar.moviecatalogue.view;


import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.fachryar.moviecatalogue.R;
import com.fachryar.moviecatalogue.adapter.TvShowRecyclerAdapter;
import com.fachryar.moviecatalogue.model.TvShow;
import com.fachryar.moviecatalogue.utils.AppConfig;
import com.fachryar.moviecatalogue.viewmodel.TvShowViewModel;

import java.util.ArrayList;

public class TvShowFragment extends Fragment {
    private RecyclerView rvTvShow;
    private TvShowRecyclerAdapter adapter;
    private ProgressBar progressBar;

    public TvShowFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tv_show, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvTvShow = view.findViewById(R.id.rv_tvShow);
        progressBar = view.findViewById(R.id.progressBarTv);

        showProgressBar();
        initRecyclerView();
        loadTvShowList();
        setHasOptionsMenu(true);

        adapter.setOnItemClickCallback(new TvShowRecyclerAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(TvShow tvShow) {
                Intent i = new Intent(getContext(), DetailActivity.class);
                i.putExtra(AppConfig.EXTRA_CATEGORY, AppConfig.CATEGORY_TV_SHOW);
                i.putExtra(AppConfig.EXTRA_TV_SHOW, tvShow);
                startActivity(i);
            }
        });
    }

    @Override
    public void onCreateOptionsMenu (Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);

        if (searchManager != null) {
            final SearchView searchView = (SearchView) (menu.findItem(R.id.menu_search)).getActionView();
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                EditText searchEditText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
                searchEditText.setTextAppearance(R.style.white_16);
            }

            searchView.setQueryHint(getResources().getString(R.string.search_tv));
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    Intent i = new Intent(getContext(), SearchActivity.class);
                    i.putExtra(AppConfig.EXTRA_CATEGORY, AppConfig.CATEGORY_TV_SHOW);
                    i.putExtra(AppConfig.EXTRA_SEARCH, query);
                    startActivity(i);
                    searchView.setQuery("",false);
                    searchView.clearFocus();
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });
        }
    }

    private void loadTvShowList(){
        showProgressBar();
        TvShowViewModel tvShowViewModel = ViewModelProviders.of(getActivity()).get(TvShowViewModel.class);
        tvShowViewModel.init();
        tvShowViewModel.getTvShows().observe(getActivity(), new Observer<ArrayList<TvShow>>() {
            @Override
            public void onChanged(ArrayList<TvShow> tvShows) {
                adapter.setListTvShow(tvShows);
                rvTvShow.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                hideProgressBar();
            }
        });
    }

    private void initRecyclerView() {
        adapter = new TvShowRecyclerAdapter();
        rvTvShow.setHasFixedSize(true);
        rvTvShow.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

}
