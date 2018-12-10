package com.framgia.quangtran.music_42.ui.search;

import com.framgia.quangtran.music_42.data.model.Track;

import java.util.List;

public interface SearchContract {
    interface Presenter {
        void searchTracks(String api);
    }

    interface View {
        void showResult(List<Track> tracks);

        void showNoResult(String message);
    }
}
