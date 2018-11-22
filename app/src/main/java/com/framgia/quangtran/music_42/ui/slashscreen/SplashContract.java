package com.framgia.quangtran.music_42.ui.slashscreen;

import android.content.ContentResolver;
import android.content.Context;

import com.framgia.quangtran.music_42.data.model.Track;

import java.util.List;

public interface SplashContract<T> {
    interface View {
        void onSuccess(List<Track> tracks);

        void onFailure(String message);
    }

    interface Presenter {
        void loadOfflineMusic(ContentResolver contentResolver);

        void loadOnlineMusic(String api);
    }
}
