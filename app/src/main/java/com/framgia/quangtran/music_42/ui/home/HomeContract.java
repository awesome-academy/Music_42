package com.framgia.quangtran.music_42.ui.home;

import com.framgia.quangtran.music_42.data.model.Genre;

import java.util.List;

public interface HomeContract {
    interface View {
        void onSuccess(List<Genre> genres);

        void onFailure(String message);
    }

    interface Presenter {
        void getGenres();
    }
}
