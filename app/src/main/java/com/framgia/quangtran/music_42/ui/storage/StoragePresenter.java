package com.framgia.quangtran.music_42.ui.storage;

import com.framgia.quangtran.music_42.data.model.Track;
import com.framgia.quangtran.music_42.data.repository.TrackRepository;
import com.framgia.quangtran.music_42.data.source.TrackDataSource;

import java.util.List;

public class StoragePresenter implements StorageContract.Presenter {
    private static final int NUMBER_ZERO = 0;
    private TrackRepository mRepository;
    private StorageContract.View mView;

    public StoragePresenter(TrackRepository repository) {
        this.mRepository = repository;
    }

    @Override
    public void setView(StorageContract.View view) {
        this.mView = view;
    }

    @Override
    public void getOfflineTracks() {
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
    public void getDownloadTracks() {
    }

    @Override
    public void getFavoriteTracks() {
        mRepository.getFavoriteTracks(new TrackDataSource.DataCallback<Track>() {
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
    public void addFavoriteTrack(Track track) {
        mRepository.addFavoriteTrack(track, new TrackDataSource.DataCallback<Boolean>() {
            @Override
            public void onSuccess(List<Boolean> datas) {
                boolean isSuccess = datas.get(NUMBER_ZERO);
                if (isSuccess) {
                    mView.addFavoriteTrackSuccess();
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
                    mView.deleteFavoriteTrackSuccess();
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
