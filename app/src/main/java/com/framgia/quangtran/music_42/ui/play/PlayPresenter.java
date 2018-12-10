package com.framgia.quangtran.music_42.ui.play;

import com.framgia.quangtran.music_42.data.model.Track;
import com.framgia.quangtran.music_42.data.repository.TrackRepository;
import com.framgia.quangtran.music_42.data.source.TrackDataSource;

import java.util.List;

public class PlayPresenter implements PlayContract.Presenter {
    private PlayContract.View mView;
    private TrackRepository mRepository;

    public PlayPresenter(TrackRepository repository, PlayContract.View view) {
        mView = view;
        mRepository = repository;
    }

    @Override
    public void addFavoriteTrack(Track track) {
    }

    @Override
    public void deleteFavoriteTrack(Track track) {
    }
}
