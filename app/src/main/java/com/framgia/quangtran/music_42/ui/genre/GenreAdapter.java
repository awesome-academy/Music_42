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

import java.util.ArrayList;
import java.util.List;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.ViewHolder> {
    private List<Track> mTracks;
    private LayoutInflater mInflater;
    private OnClickItemListener mListener;

    public GenreAdapter(List<Track> tracks, OnClickItemListener listener) {
        this.mTracks = tracks;
        this.mListener = listener;
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
        viewHolder.bindData(mTracks, i, mListener);
    }

    @Override
    public int getItemCount() {
        return mTracks != null ? mTracks.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mTextTrackName;
        private TextView mTextSingerName;
        private ImageView mImageTrack;
        private ImageView mImageFavorite;
        private ImageView mImageDownload;
        private View mView;
        private OnClickItemListener mListener;
        private List<Track> mTracks;
        private int mPosition;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextTrackName = itemView.findViewById(R.id.text_name_track);
            mTextSingerName = itemView.findViewById(R.id.text_name_singer);
            mImageTrack = itemView.findViewById(R.id.image_icon);
            mImageDownload = itemView.findViewById(R.id.image_download);
            mImageFavorite = itemView.findViewById(R.id.image_favorite);
            mView = itemView.findViewById(R.id.view_track);
            mTracks = new ArrayList<>();
            setOnClickListener();
        }

        private void setOnClickListener() {
            mView.setOnClickListener(this);
            mImageDownload.setOnClickListener(this);
            mImageFavorite.setOnClickListener(this);
        }

        public void bindData(List<Track> tracks, int i, OnClickItemListener listener) {
            mListener = listener;
            mPosition = i;
            if (tracks.get(i) != null) {
                mTracks = tracks;
                mTextTrackName.setText(tracks.get(i).getTitle());
                mTextSingerName.setText(tracks.get(i).getUserName());
                RequestOptions requestOptions = new RequestOptions();
                requestOptions.error(R.drawable.soundcloud);
                Glide.with(mImageTrack.getContext())
                        .load(tracks.get(i).getArtWorkUrl())
                        .apply(requestOptions)
                        .into(mImageTrack);
            }
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.view_track:
                    mListener.clickViewTrack(mPosition, mTracks);
                    break;
                case R.id.image_favorite:
                    mListener.clickFavorite(mTracks.get(mPosition), mImageFavorite);
                    break;
                case R.id.image_download:
                    mListener.clickDownload(mTracks.get(mPosition), mImageDownload);
                    break;
                default:
                    break;
            }
        }
    }

    public void updateTracks(List<Track> tracks) {
        if (tracks != null) {
            mTracks.clear();
            mTracks.addAll(tracks);
            notifyDataSetChanged();
        }
    }

    public void addTracks(List<Track> tracks) {
        if (tracks != null) {
            mTracks.addAll(tracks);
            notifyDataSetChanged();
        }
    }

    interface OnClickItemListener {
        void clickViewTrack(int i, List<Track> tracks);

        void clickFavorite(Track track, ImageView imageView);

        void clickDownload(Track track, ImageView imageView);
    }
}
