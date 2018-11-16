package com.framgia.quangtran.music_42.ui.genre;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.framgia.quangtran.music_42.R;
import com.framgia.quangtran.music_42.data.model.Track;

import java.util.List;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.ViewHolder> {
    private List<Track> mTracks;
    private LayoutInflater mInflater;

    public GenreAdapter(List<Track> tracks) {
        this.mTracks = tracks;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (mInflater == null) {
            mInflater = LayoutInflater.from(viewGroup.getContext());
        }
        View contactView = mInflater.inflate(R.layout.item_recycler_music_genre, viewGroup,
                false);
        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.bindData(mTracks.get(i));
    }

    @Override
    public int getItemCount() {
        return mTracks != null ? mTracks.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextTrackName;
        private TextView mTextSingerName;
        private ImageView mImageTrack;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextTrackName = itemView.findViewById(R.id.text_name_track);
            mTextSingerName = itemView.findViewById(R.id.text_name_singer);
            mImageTrack = itemView.findViewById(R.id.image_track);
        }

        public void bindData(Track track) {
            if (track == null) return;
            mTextTrackName.setText(track.getTitle());
            mTextSingerName.setText(track.getUserName());
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.error(R.drawable.soundcloud);
            Glide.with(mImageTrack.getContext())
                    .load(track.getArtWorkUrl())
                    .apply(requestOptions)
                    .into(mImageTrack);
        }
    }
}
