package com.framgia.quangtran.music_42.data.source.genre;

import com.framgia.quangtran.music_42.R;
import com.framgia.quangtran.music_42.data.model.Genre;
import com.framgia.quangtran.music_42.data.model.GenreKey;
import com.framgia.quangtran.music_42.data.model.GenreName;
import com.framgia.quangtran.music_42.data.source.GenreDataSource;

import java.util.ArrayList;
import java.util.List;

public class GenreLocalDataSource implements GenreDataSource.LocalGenre {
    private static GenreLocalDataSource sInstance;

    private GenreLocalDataSource() {
    }

    public static GenreLocalDataSource getInstance() {
        if (sInstance == null) {
            sInstance = new GenreLocalDataSource();
        }
        return sInstance;
    }

    @Override
    public void getGenres(GenreDataSource.DataCallback<Genre> callback) {
        List<Genre> genres = new ArrayList<>();
        genres.add(new Genre(GenreName.ALL_MUSIC,
                GenreKey.ALL_MUSIC, R.drawable.allmusic));
        genres.add(new Genre(GenreName.ALL_AUDIO,
                GenreKey.ALL_AUDIO, R.drawable.alldaudio));
        genres.add(new Genre(GenreName.ALTERNATIVE,
                GenreKey.ALTERNATIVE, R.drawable.alternative));
        genres.add(new Genre(GenreName.AMBIENT,
                GenreKey.AMBIENT, R.drawable.ambient));
        genres.add(new Genre(GenreName.CLASSICAL,
                GenreKey.CLASSICAL, R.drawable.classical));
        genres.add(new Genre(GenreName.COUNTRY,
                GenreKey.COUNTRY, R.drawable.country));
            callback.onSuccess(genres);
    }
}
