package com.framgia.quangtran.music_42.ui.personal;

import com.framgia.quangtran.music_42.data.model.Track;

import java.util.List;

public interface PersonalContract {
    interface View {
        void onSuccessId(List<Long> idTracks);

        void onRecentTracksSuccess(List<Track> idTracks);

        void onFailure(String message);
    }

    interface Presenter {
        void setView(View view);

        void getRecentTracks();

        void loadRecentTracks(String api);
    }
}
