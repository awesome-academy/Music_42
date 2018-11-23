package com.framgia.quangtran.music_42.ui.splashscreen;

import com.framgia.quangtran.music_42.data.model.Track;
import com.framgia.quangtran.music_42.data.repository.TrackRepository;
import com.framgia.quangtran.music_42.data.source.TrackDataSource;

import java.util.List;

public class SplashPresenter implements SplashContract.Presenter {
    private TrackRepository mRepository;
    private SplashContract.View mView;

    public SplashPresenter(TrackRepository repository, SplashActivity view) {
        this.mRepository = repository;
        this.mView = view;
    }

    @Override
    public void loadOnlineMusic(String api) {
        mRepository.getOnlineTrack(api, new TrackDataSource.DataCallback<Track>() {
            @Override
            public void onSuccess(List<Track> tracks) {
                mView.onSuccess(tracks);
            }

            @Override
            public void onFailed(String message) {
                mView.onFailure(message);
            }
        });
    }
}
