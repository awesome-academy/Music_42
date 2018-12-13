package com.framgia.quangtran.music_42.ui.storage;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.framgia.quangtran.music_42.R;
import com.framgia.quangtran.music_42.data.model.Track;
import com.framgia.quangtran.music_42.ui.storage.contract.StorageStyle;

import java.util.List;

public class StorageAdapter extends RecyclerView.Adapter<StorageAdapter.ViewHolder> {
    public static String sKey = "";
    private List<Track> mTracks;
    private LayoutInflater mInflater;
    private StorageClickListener mListener;

    public StorageAdapter(String key, List<Track> tracks, StorageClickListener clickListener) {
        mListener = clickListener;
        mTracks = tracks;
        sKey = key;
    }

    @NonNull
    @Override
    public StorageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (mInflater == null) {
            mInflater = LayoutInflater.from(viewGroup.getContext());
        }
        View contactView = mInflater.inflate(R.layout.item_recycler_music_personal, viewGroup,
                false);
        return new StorageAdapter.ViewHolder(contactView, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull StorageAdapter.ViewHolder viewHolder, int i) {
        viewHolder.bindData(mTracks.get(i), i);
    }

    @Override
    public int getItemCount() {
        return mTracks != null ? mTracks.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mTextTrackName;
        private TextView mTextSingerName;
        private View mViewTrack;
        private ImageView mImageFavorite;
        private StorageClickListener mListener;
        private int mTrackPosition;
        private Track mTrack;

        public ViewHolder(@NonNull View itemView, StorageClickListener storageClickListener) {
            super(itemView);
            mTrack = new Track();
            mListener = storageClickListener;
            mViewTrack = itemView.findViewById(R.id.view_track);
            mTextTrackName = itemView.findViewById(R.id.text_name_track);
            mTextSingerName = itemView.findViewById(R.id.text_name_singer);
            mImageFavorite = itemView.findViewById(R.id.image_personal_favorite);
            mImageFavorite.setOnClickListener(this);
            mViewTrack.setOnClickListener(this);
            if(StorageAdapter.sKey.equals(StorageStyle.StorageKey.FAVORITE)){
                mImageFavorite.setImageResource(R.drawable.ic_minus);
            }
        }

        public void bindData(final Track track, final int i) {
            mTrackPosition = i;
            mTrack = track;
            mTextTrackName.setText(track.getTitle());
            mTextSingerName.setText(track.getUserName());
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.view_track:
                    mListener.onClickPlayMusic(mTrackPosition);
                    break;
                case R.id.image_personal_favorite:
                    mListener.onClickImageFavorite(mTrack, mTrackPosition);
                    break;
                default:
                    break;
            }
        }
    }

    public interface StorageClickListener {
        void onClickPlayMusic(int i);

        void onClickImageFavorite(Track track, int i);
    }
}
