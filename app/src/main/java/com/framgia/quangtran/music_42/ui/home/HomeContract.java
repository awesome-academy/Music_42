package com.framgia.quangtran.music_42.ui.home;

import com.framgia.quangtran.music_42.data.model.Genre;

import java.util.List;

public interface HomeContract {
    interface Presenter {
        List<Genre> getGenres();
    }
}
