package com.framgia.quangtran.music_42.ui.genre;

import com.framgia.quangtran.music_42.data.model.Track;
import com.framgia.quangtran.music_42.data.repository.TrackRepository;
import com.framgia.quangtran.music_42.data.source.TrackDataSource;

import java.util.List;

public class GenrePresenter implements GenreContract.Presenter {
    private TrackRepository mRepository;
    private GenreContract.View mView;

    public GenrePresenter(TrackRepository repository) {
        this.mRepository = repository;
    }


    @Override
    public void setView(GenreContract.View view) {
        mView = view;
    }

    @Override
    public void getGenres(String api) {
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
