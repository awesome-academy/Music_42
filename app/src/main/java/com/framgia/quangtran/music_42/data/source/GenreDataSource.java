package com.framgia.quangtran.music_42.data.source;

import com.framgia.quangtran.music_42.data.model.Genre;

import java.util.List;

public interface GenreDataSource {
    interface DataCallback<T> {
        void onSuccess(List<T> data);

        void onFailure(String message);
    }

    interface LocalGenre {
        void getGenres(GenreDataSource.DataCallback<Genre> callback);
    }
}
