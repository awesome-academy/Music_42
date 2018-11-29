package com.framgia.quangtran.music_42.ui.storage;

import com.framgia.quangtran.music_42.data.model.Track;

import java.util.List;

public interface StorageContract {
    interface View {
        void onSuccess(List<Track> data);

        void onFailure(String message);
    }

    interface Presenter {
        void setView(StorageContract.View view);

        void loadOfflineMusic();

        void getDownloadTrack();

        void getFavoriteTrack();
    }
}
