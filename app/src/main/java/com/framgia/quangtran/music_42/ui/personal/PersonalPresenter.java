package com.framgia.quangtran.music_42.ui.personal;

import android.content.ContentResolver;

import com.framgia.quangtran.music_42.data.model.Track;
import com.framgia.quangtran.music_42.data.repository.TrackRepository;
import com.framgia.quangtran.music_42.data.source.TrackDataSource;

import java.util.List;

public class PersonalPresenter implements PersonalContract.Presenter {
    private TrackRepository mRepository;
    private PersonalContract.View mView;
    private ContentResolver mContentResolver;

    public PersonalPresenter(TrackRepository repository, ContentResolver contentResolver) {
        this.mRepository = repository;
        this.mContentResolver = contentResolver;
    }

    @Override
    public void setView(PersonalContract.View personalFragment) {
        this.mView = personalFragment;
    }

    @Override
    public void loadOfflineMusic() {
        mRepository.getOfflineTracks(mContentResolver, new TrackDataSource.DataCallback<Track>() {
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
}
