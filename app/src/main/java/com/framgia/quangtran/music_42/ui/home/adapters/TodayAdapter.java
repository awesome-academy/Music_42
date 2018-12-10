package com.framgia.quangtran.music_42.ui.home.adapters;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
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

public class TodayAdapter extends RecyclerView.Adapter<TodayAdapter.ViewHolder> {
    private List<Track> mTracks;
    private LayoutInflater mInflater;
    private ClickTrackElement mClickTrackElement;

    public TodayAdapter(ClickTrackElement trackElement) {
        mTracks = new ArrayList<>();
        mClickTrackElement = trackElement;
    }

    public TodayAdapter(List<Track> tracks, ClickTrackElement clickTrackElement) {
        this.mTracks = tracks;
        this.mClickTrackElement = clickTrackElement;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (mInflater == null) {
            mInflater = LayoutInflater.from(viewGroup.getContext());
        }
        View contactView = mInflater.inflate(R.layout.item_recycler_music_home, viewGroup,
                false);
        return new ViewHolder(contactView, mTracks, mClickTrackElement);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.bindData(mTracks.get(i), i);
    }

    @Override
    public int getItemCount() {
        return mTracks != null ? mTracks.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextTrackName;
        private TextView mTextSingerName;
        private ImageView mImageTrack;
        private ConstraintLayout mLayout;
        private ClickTrackElement mClickTrackElement;
        private List<Track> mTracks;

        public ViewHolder(@NonNull View itemView, List<Track> tracks, ClickTrackElement clickTrackElement) {
            super(itemView);
            mClickTrackElement = clickTrackElement;
            mTracks = tracks;
            mLayout = itemView.findViewById(R.id.constraint_track);
            mTextTrackName = itemView.findViewById(R.id.text_name_track);
            mTextSingerName = itemView.findViewById(R.id.text_name_singer);
            mImageTrack = itemView.findViewById(R.id.image_track);
        }

        public void bindData(Track track, final int position) {
            if (track != null) {
                mTextTrackName.setText(track.getTitle());
                mTextSingerName.setText(track.getUserName());
                RequestOptions requestOptions = new RequestOptions();
                requestOptions.error(R.drawable.soundcloud);
                Glide.with(mImageTrack.getContext())
                        .load(track.getArtWorkUrl())
                        .apply(requestOptions.circleCrop())
                        .into(mImageTrack);
                mLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mClickTrackElement.onClickTrack(mTracks, position);
                    }
                });
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

    public interface ClickTrackElement {
        void onClickTrack(List<Track> tracks, int i);
    }
}
