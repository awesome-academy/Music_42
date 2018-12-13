package com.framgia.quangtran.music_42.ui.play;

import com.framgia.quangtran.music_42.data.model.Track;
import com.framgia.quangtran.music_42.data.repository.TrackRepository;
import com.framgia.quangtran.music_42.data.source.TrackDataSource;

import java.util.List;

public class PlayPresenter implements PlayContract.Presenter {
    private static final int NUMBER_ZERO = 0;
    private PlayContract.View mView;
    private TrackRepository mRepository;

    public PlayPresenter(TrackRepository repository, PlayContract.View view) {
        mView = view;
        mRepository = repository;
    }

    @Override
    public void addFavoriteTrack(Track track) {
        mRepository.addFavoriteTrack(track, new TrackDataSource.DataCallback<Boolean>() {
            @Override
            public void onSuccess(List<Boolean> datas) {
                boolean isSuccess = datas.get(NUMBER_ZERO);
                if (isSuccess) {
                    mView.onFavoriteTrackSuccess();
                    return;
                }
            }

            @Override
            public void onFailed(String message) {
                mView.onFailure(message);
            }
        });
    }

    @Override
    public void deleteFavoriteTrack(Track track) {
        mRepository.deleteFavoriteTrack(track, new TrackDataSource.DataCallback<Boolean>() {
            @Override
            public void onSuccess(List<Boolean> datas) {
                boolean isSuccess = datas.get(NUMBER_ZERO);
                if (isSuccess) {
                    mView.onDeleteTrackSuccess();
                    return;
                }
            }

            @Override
            public void onFailed(String message) {
                mView.onFailure(message);
            }
        });
    }
}
