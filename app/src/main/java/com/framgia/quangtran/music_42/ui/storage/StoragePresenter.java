package com.framgia.quangtran.music_42.ui.storage;

import android.content.ContentResolver;

import com.framgia.quangtran.music_42.data.model.Track;
import com.framgia.quangtran.music_42.data.repository.TrackRepository;
import com.framgia.quangtran.music_42.data.source.TrackDataSource;

import java.util.List;

public class StoragePresenter implements StorageContract.Presenter {
    private TrackRepository mRepository;
    private StorageContract.View mView;
    private ContentResolver mContentResolver;

    public StoragePresenter(TrackRepository repository, ContentResolver contentResolver) {
        this.mRepository = repository;
        this.mContentResolver = contentResolver;
    }

    @Override
    public void setView(StorageContract.View view) {
        this.mView = view;
    }

    @Override
    public void loadOfflineMusic() {
        mRepository.getOfflineTracks(new TrackDataSource.DataCallback<Track>() {
            @Override
            public void onSuccess(List<Track> data) {
                mView.onSuccess(data);
            }

            @Override
            public void onFailed(String message) {
                mView.onFailure(message);
            }
        });
    }

    @Override
    public void getDownloadTrack() {
    }

    @Override
    public void getFavoriteTrack() {
    }
}
