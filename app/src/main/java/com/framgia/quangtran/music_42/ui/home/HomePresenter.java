package com.framgia.quangtran.music_42.ui.home;

import com.framgia.quangtran.music_42.R;
import com.framgia.quangtran.music_42.data.model.Genre;
import com.framgia.quangtran.music_42.data.model.GenreKey;
import com.framgia.quangtran.music_42.data.model.GenreName;

import java.util.ArrayList;
import java.util.List;

public class HomePresenter implements HomeContract.Presenter {
    @Override
    public List<Genre> getGenres() {
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
        return genres;
    }
}
