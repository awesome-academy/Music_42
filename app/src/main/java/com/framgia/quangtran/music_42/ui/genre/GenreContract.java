package com.framgia.quangtran.music_42.ui.genre;

import com.framgia.quangtran.music_42.data.model.Track;

import java.util.List;

public interface GenreContract {
    interface View{
        void onSuccess(List<Track> tracks);

        void onFailure(String message);
    }

    interface Presenter{
        void setView(View view);
        void getGenres(String api);
    }
}
