package com.framgia.quangtran.music_42.ui.homescreen.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.framgia.quangtran.music_42.R;
import com.framgia.quangtran.music_42.data.model.Genre;

import java.util.List;

public class GenresAdapter extends RecyclerView.Adapter<GenresAdapter.ViewHolder> {
    private List<Genre> mGenres;
    private LayoutInflater inflater;

    public GenresAdapter(List<Genre> genres) {
        this.mGenres = genres;
    }

    @NonNull
    @Override
    public GenresAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (inflater == null) {
            inflater = LayoutInflater.from(viewGroup.getContext());
        }
        View contactView = inflater.inflate(R.layout.item_recycler_genres_home, viewGroup,
                false);
        return new GenresAdapter.ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull GenresAdapter.ViewHolder viewHolder, int i) {
        viewHolder.bindData(mGenres.get(i));
    }

    @Override
    public int getItemCount() {
        return mGenres != null ? mGenres.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextGenreName;
        private ImageView mImageGenre;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextGenreName = itemView.findViewById(R.id.text_name_genre);
            mImageGenre = itemView.findViewById(R.id.image_genre);
        }

        private void bindData(Genre genre) {
            mImageGenre.setImageResource(genre.getImageUrl());
            mTextGenreName.setText(genre.getName());
        }
    }
}
