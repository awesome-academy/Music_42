package com.framgia.quangtran.music_42.data.repository;

import com.framgia.quangtran.music_42.data.model.Genre;
import com.framgia.quangtran.music_42.data.source.GenreDataSource;

public class GenreRepository implements GenreDataSource.LocalGenre {
    private static GenreRepository sInstance;
    private GenreDataSource.LocalGenre mDataSource;

    private GenreRepository(GenreDataSource.LocalGenre localGenre) {
        mDataSource = localGenre;
    }

    public static synchronized GenreRepository getInstance(GenreDataSource.LocalGenre localGenre) {
        if (sInstance == null) {
            sInstance = new GenreRepository(localGenre);
        }
        return sInstance;
    }

    @Override
    public void getGenres(GenreDataSource.DataCallback<Genre> callback) {
        mDataSource.getGenres(callback);
    }
}
