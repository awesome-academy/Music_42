package com.framgia.quangtran.music_42.ui.splashscreen;

import com.framgia.quangtran.music_42.data.model.Track;

import java.util.List;

public interface SplashContract<T> {
    interface View {
        void onSuccess(List<Track> tracks);

        void onFailure(String message);
    }

    interface Presenter {
        void loadOnlineMusic(String api);
    }
}
