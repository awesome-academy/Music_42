package com.framgia.quangtran.music_42.ui.splash;

import com.framgia.quangtran.music_42.data.model.Track;

import java.util.List;

public interface SplashContract {
    interface View {
        void onSuccess(List<Track> tracks);

        void onFailure(String message);
    }

    interface Presenter {
        void loadOnlineMusic(String api);
    }
}
