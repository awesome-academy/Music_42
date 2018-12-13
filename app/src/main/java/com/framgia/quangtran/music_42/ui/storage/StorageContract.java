package com.framgia.quangtran.music_42.ui.storage;

import com.framgia.quangtran.music_42.data.model.Track;

import java.util.List;

public interface StorageContract {
    interface View {
        void onSuccess(List<Track> data);

        void onFailure(String message);

        void addFavoriteTrackSuccess();

        void deleteFavoriteTrackSuccess();
    }

    interface Presenter {
        void setView(StorageContract.View view);

        void getOfflineTracks();

        void getDownloadTracks();

        void getFavoriteTracks();

        void addFavoriteTrack(Track track);

        void deleteFavoriteTrack(Track track);
    }
}
