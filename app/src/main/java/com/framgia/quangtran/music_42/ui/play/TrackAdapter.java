package com.framgia.quangtran.music_42.ui.play;

import android.graphics.Color;
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

import java.util.Collections;
import java.util.List;

public class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.ViewHolder> {
    public static final int INDEX_UNIT = 1;
    private List<Track> mTracks;
    private OnDragDropListener mDragDropListener;
    private LayoutInflater mInflater;
    private ClickTrackElement mClickTrackElement;

    public TrackAdapter(List<Track> tracks, ClickTrackElement clickTrackElement, OnDragDropListener dropListener) {
        this.mTracks = tracks;
        this.mClickTrackElement = clickTrackElement;
        this.mDragDropListener = dropListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (mInflater == null) {
            mInflater = LayoutInflater.from(viewGroup.getContext());
        }
        View contactView = mInflater.inflate(R.layout.item_recycler_music_home, viewGroup,
                false);
        return new ViewHolder(contactView, mClickTrackElement);
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

        public ViewHolder(@NonNull View itemView, ClickTrackElement clickTrackElement) {
            super(itemView);
            mClickTrackElement = clickTrackElement;
            mLayout = itemView.findViewById(R.id.constraint_track);
            mTextTrackName = itemView.findViewById(R.id.text_name_track);
            mTextSingerName = itemView.findViewById(R.id.text_name_singer);
            mImageTrack = itemView.findViewById(R.id.image_track);
            updateItem();
        }

        public void bindData(final Track track, final int position) {
            if (track != null) {
                mTextTrackName.setText(track.getTitle());
                mTextSingerName.setText(track.getUserName());
                RequestOptions requestOptions = new RequestOptions();
                requestOptions.error(R.drawable.soundcloud);
                Glide.with(mImageTrack.getContext())
                        .load(track.getArtWorkUrl())
                        .apply(requestOptions)
                        .into(mImageTrack);
                mLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mClickTrackElement.onClickTrack(position);
                    }
                });
            }
        }

        private void updateItem() {
            mTextTrackName.setTextColor(Color.WHITE);
            mTextSingerName.setTextColor(Color.WHITE);
            itemView.setBackgroundColor(itemView.getResources().getColor(R.color.black_transparent));
        }

    }

    public void onMove(int oldPosition, int newPosition) {
        if (oldPosition < newPosition) {
            for (int i = oldPosition; i < newPosition; i++) {
                Collections.swap(mTracks, i, i + INDEX_UNIT);
            }
        } else {
            for (int i = oldPosition; i > newPosition; i--) {
                Collections.swap(mTracks, i, i - INDEX_UNIT);
            }
        }
        notifyItemMoved(oldPosition, newPosition);
        mDragDropListener.onDropViewHolder(mTracks);
    }

    public void swipe(int position) {
        if (mTracks.size() > INDEX_UNIT) {
            mTracks.remove(position);
            notifyItemRemoved(position);
            mDragDropListener.onSwipeViewHolder(mTracks);
            return;
        }
        notifyDataSetChanged();
    }

    public interface ClickTrackElement {
        void onClickTrack(int i);
    }

    public interface OnDragDropListener {
        void onDropViewHolder(List<Track> tracks);

        void onSwipeViewHolder(List<Track> tracks);
    }
}
