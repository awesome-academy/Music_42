package com.framgia.quangtran.music_42.ui.personal;

import android.content.Context;

import com.framgia.quangtran.music_42.data.model.Track;
import com.framgia.quangtran.music_42.data.repository.TrackRepository;
import com.framgia.quangtran.music_42.data.source.TrackDataSource;

import java.util.List;

public class PersonalPresenter implements PersonalContract.Presenter {
    private TrackRepository mRepository;
    private PersonalContract.View mView;
    private Context mContext;

    public PersonalPresenter(Context context, TrackRepository repository) {
        this.mRepository = repository;
        this.mContext = context;
    }

    @Override
    public void setView(PersonalContract.View personalFragment) {
        this.mView = personalFragment;
    }

    @Override
    public void getRecentTracks() {
        mRepository.getRecentTrack(mContext, new TrackDataSource.DataCallback<Long>() {
            @Override
            public void onSuccess(List<Long> idTracks) {
                mView.onSuccessId(idTracks);
            }

            @Override
            public void onFailed(String message) {
                mView.onFailure(message);
            }
        });
    }

    @Override
    public void loadRecentTracks(String api) {
        mRepository.getDetailTrack(api, new TrackDataSource.DataCallback<Track>() {
            @Override
            public void onSuccess(List<Track> data) {
                mView.onRecentTracksSuccess(data);
            }

            @Override
            public void onFailed(String message) {
                mView.onFailure(message);
            }
        });
    }
}
