package com.framgia.quangtran.music_42.ui.play;

import com.framgia.quangtran.music_42.data.model.Track;

public interface PlayContract {
    interface View {
        void onFailure(String message);

        void onDeleteTrackSuccess();

        void onFavoriteTrackSuccess();
    }

    interface Presenter {
        void addFavoriteTrack(Track track);

        void deleteFavoriteTrack(Track track);
    }
}
